package communication.gateway;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import constants.Constants;
import dto.account.LoginDTO;
import dto.account.RegisterDTO;
import model.Car;
import model.User;
import services.application.ApplicationInterface;
import services.application.ApplicationService;
import services.authentication.AuthInterface;
import services.authentication.AuthService;
import types.Types.AccountType;
import types.Types.CarCategory;
import types.Types.CarSearchType;
import java.util.ArrayList;

public class GatewayThread extends Thread {
    private ObjectInputStream objectInputStream;
    private Socket clientConnectionSocket;

    public GatewayThread(ObjectInputStream objectInputStream, Socket clientConnectionSocket) {
        this.objectInputStream = objectInputStream;

        this.clientConnectionSocket = clientConnectionSocket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientConnectionSocket.getOutputStream());

            new AuthService();
            Registry authRegistry = LocateRegistry.getRegistry(Constants.AUTH_REGISTRY_PORT);
            var authStub = (AuthInterface) authRegistry.lookup(Constants.AUTH_SERVICE_NAME);

            while (true) {
                int loginOrRegisterSelected = objectInputStream.readInt();

                switch (loginOrRegisterSelected) {
                    case 1:
                        User loggedUser = null;

                        LoginDTO loginDTO = (LoginDTO) objectInputStream.readObject();
                        loggedUser = authStub.authenticate(loginDTO);

                        objectOutputStream.writeObject(loggedUser);
                        objectOutputStream.flush();

                        if (loggedUser == null)
                            continue;

                        new ApplicationService();
                        Registry appRegistry = LocateRegistry.getRegistry(Constants.APP_REGISTRY_PORT);
                        var appStub = (ApplicationInterface) appRegistry.lookup(Constants.APP_SERVICE_NAME);

                        int selectedOption = 1;
                        while (selectedOption != 0) {
                            selectedOption = objectInputStream.readInt();

                            switch (selectedOption) {
                                case 1:
                                    CarSearchType searchType = (CarSearchType) objectInputStream.readObject();
                                    CarCategory searchCategory = (CarCategory) objectInputStream.readObject();

                                    ArrayList<Car> carList = appStub.getAllCars(searchType, searchCategory);
                                    objectOutputStream.writeObject(carList);
                                    objectOutputStream.flush();
                                    break;
                                case 2:
                                    CarSearchType carSearchType = (CarSearchType) objectInputStream.readObject();
                                    String searchTerm = objectInputStream.readUTF();

                                    Car foundedCar = appStub.getCar(searchTerm, carSearchType);

                                    objectOutputStream.writeObject(foundedCar);
                                    objectOutputStream.flush();

                                    break;
                                case 3:
                                    int availableCarsQuantity = appStub.getCarsQuantity();
                                    objectOutputStream.writeInt(availableCarsQuantity);
                                    objectOutputStream.flush();
                                    break;
                                case 4:
                                    CarSearchType buyCarSearchType = (CarSearchType) objectInputStream.readObject();
                                    String buyCarSearchTerm = objectInputStream.readUTF();

                                    Car boughtFoundedCar = appStub.buyCar(loggedUser.getId(), buyCarSearchTerm,
                                            buyCarSearchType);

                                    objectOutputStream.writeObject(boughtFoundedCar);
                                    objectOutputStream.flush();
                                    break;

                                case 5:
                                    if (loggedUser.getAccountType() == AccountType.EMPLOYEE) {
                                        Car createdCar = (Car) objectInputStream.readObject();
                                        appStub.postCar(createdCar);
                                    }
                                    break;
                                case 6:
                                    if (loggedUser.getAccountType() == AccountType.EMPLOYEE) {
                                        int deletionType = objectInputStream.readInt();
                                        String deletableCarName = objectInputStream.readUTF();

                                        boolean hasDeleted = appStub.deleteCar(deletionType, deletableCarName);
                                        objectOutputStream.writeBoolean(hasDeleted);
                                        objectOutputStream.flush();
                                    }
                                    break;
                                case 7:
                                    if (loggedUser.getAccountType() == AccountType.EMPLOYEE) {
                                        CarSearchType updateCarSearchType = (CarSearchType) objectInputStream
                                                .readObject();
                                        String updateCarSearchTerm = objectInputStream.readUTF();

                                        int foundedCarIndex = appStub.getCarIndex(updateCarSearchTerm,
                                                updateCarSearchType);
                                        objectOutputStream.writeInt(foundedCarIndex);
                                        objectOutputStream.flush();

                                        if (foundedCarIndex == -1)
                                            break;

                                        Car updatedCar = (Car) objectInputStream.readObject();
                                        appStub.putCar(updatedCar, foundedCarIndex);
                                        break;
                                    }
                                    break;
                                case 0:
                                    continue;
                                default:
                                    break;
                            }
                        }

                        break;
                    case 2:
                        RegisterDTO registerDTO = (RegisterDTO) objectInputStream.readObject();
                        authStub.register(registerDTO);
                        break;
                    case 0:
                        objectInputStream.close();
                        clientConnectionSocket.close();
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException | NotBoundException e) {
            e.printStackTrace();
        }

    }
}
