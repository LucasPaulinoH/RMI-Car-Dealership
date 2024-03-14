package communication.gateway;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import dto.account.LoginDTO;
import dto.account.RegisterDTO;
import model.Car;
import model.User;
import services.application.ApplicationInterface;
import services.authentication.AuthInterface;
import types.Types.AccountType;
import types.Types.CarCategory;
import types.Types.CarSearchType;
import java.util.ArrayList;

public class GatewayThread extends Thread {
    private ObjectInputStream objectInputStream;
    private Socket clientConnectionSocket;
    private AuthInterface authStub;
    private ApplicationInterface appStub;

    public GatewayThread(ObjectInputStream objectInputStream, Socket clientConnectionSocket, AuthInterface authStub,
            ApplicationInterface appStub) {
        this.objectInputStream = objectInputStream;
        this.clientConnectionSocket = clientConnectionSocket;
        this.authStub = authStub;
        this.appStub = appStub;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientConnectionSocket.getOutputStream());

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
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
