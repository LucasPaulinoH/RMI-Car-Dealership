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

                                    ArrayList<Car> foundedCars = appStub.getCar(searchTerm, carSearchType);

                                    objectOutputStream.writeObject(foundedCars);
                                    objectOutputStream.flush();

                                    break;
                                case 3:
                                    int availableCarsQuantity = appStub.getCarsQuantity();
                                    objectOutputStream.writeInt(availableCarsQuantity);
                                    objectOutputStream.flush();
                                    break;
                                case 4:
                                    String purchasedCarRenavam = objectInputStream.readUTF();

                                    boolean hasPurchased = appStub.buyCar(purchasedCarRenavam);

                                    objectOutputStream.writeBoolean(hasPurchased);
                                    objectOutputStream.flush();
                                    break;

                                case 5:
                                    if (loggedUser.getAccountType() == AccountType.EMPLOYEE) {
                                        Car createdCarDTO = (Car) objectInputStream.readObject();
                                        Car createdCarResult = appStub.postCar(createdCarDTO);

                                        objectOutputStream.writeObject(createdCarResult);
                                        objectOutputStream.flush();
                                    }
                                    break;
                                case 6:
                                    if (loggedUser.getAccountType() == AccountType.EMPLOYEE) {
                                        CarSearchType deleteCarSearchType = (CarSearchType) objectInputStream.readObject();
                                        String deleteCarSearchTerm = objectInputStream.readUTF();

                                        boolean hasDeleted = appStub.deleteCar(deleteCarSearchType, deleteCarSearchTerm);
                                        objectOutputStream.writeBoolean(hasDeleted);
                                        objectOutputStream.flush();
                                    }
                                    break;
                                case 7:
                                    if (loggedUser.getAccountType() == AccountType.EMPLOYEE) {
                                        String searchedCarRenavam = objectInputStream.readUTF();

                                        int foundedCarIndex = appStub.getCarIndexFromRenavam(searchedCarRenavam);

                                        objectOutputStream.writeInt(foundedCarIndex);
                                        objectOutputStream.flush();

                                        if (foundedCarIndex == -1)
                                            break;

                                        Car updatedCar = (Car) objectInputStream.readObject();
                                        boolean hasUpdated = appStub.putCar(updatedCar, foundedCarIndex);

                                        objectOutputStream.writeBoolean(hasUpdated);
                                        objectOutputStream.flush();
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
