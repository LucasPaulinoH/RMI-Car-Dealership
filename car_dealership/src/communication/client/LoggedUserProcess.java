package communication.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import model.Car;
import model.User;
import types.Types.AccountType;
import types.Types.CarCategory;
import types.Types.CarSearchType;
import utils.TerminalPrints;

public class LoggedUserProcess {
    private User loggedUser;
    private AccountType accountType;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Scanner sc = new Scanner(System.in);

    public LoggedUserProcess(User loggedUser, AccountType accountType, ObjectInputStream objectInputStream,
            ObjectOutputStream objectOutputStream) {
        this.loggedUser = loggedUser;
        this.accountType = accountType;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.execute();
    }

    private void execute() {
        switch (accountType) {
            case CUSTOMER:
                executeCustomerProcess();
                break;
            case EMPLOYEE:
                executeEmployeeProcess();
                break;
        }
    }

    private void executeCustomerProcess() {
        System.out.println("Executing customer process.");
    }

    private void executeEmployeeProcess() {
        try {
            while (true) {
                TerminalPrints.printLoggedEmployeeOptions();
                int selectedOption = sc.nextInt();

                objectOutputStream.writeInt(selectedOption);
                objectOutputStream.flush();

                switch (selectedOption) {
                    case 1:
                        System.out.println("Car name > ");
                        String newCarName = sc.next();

                        int categoryInt;
                        CarCategory category = null;
                        while (category == null) {
                            System.out.println("Category (1 - economic, 2 - intermediate, 3 - executive) > ");
                            categoryInt = sc.nextInt();

                            switch (categoryInt) {
                                case 1:
                                    category = CarCategory.ECONOMIC;
                                    break;
                                case 2:
                                    category = CarCategory.INTERMEDIATE;
                                    break;
                                case 3:
                                    category = CarCategory.EXECUTIVE;
                                    break;
                                default:
                                    System.out.println(
                                            "Invalid category selected. Try 1 for Economic, 2 for Intermediate and 3 for Executive.");
                                    continue;
                            }
                        }

                        System.out.println("Renavam > ");
                        String renavam = sc.next();

                        System.out.println("Manufacture year > ");
                        String manufactureYear = sc.next();

                        System.out.println("Price (R$) > ");
                        double price = sc.nextDouble();

                        System.out.println("Quantity > ");
                        int quantity = sc.nextInt();

                        Car newCar = new Car(newCarName, category, renavam, manufactureYear, price, quantity);

                        objectOutputStream.writeObject(newCar);
                        objectOutputStream.flush();

                        continue;
                    case 2:
                        System.out.println("Car name > ");
                        String deletedCarName = sc.next();

                        objectOutputStream.writeUTF(deletedCarName);
                        objectOutputStream.flush();
                        break;
                    case 3:

                        break;

                    case 4:
                        break;

                    case 5:
                        int searchTypeInt;
                        CarSearchType carSearchType = null;

                        while (carSearchType == null) {
                            System.out.println("Search type (1 - name, 2 - renavam) > ");
                            searchTypeInt = sc.nextInt();

                            switch (searchTypeInt) {
                                case 1:
                                    carSearchType = CarSearchType.NAME;
                                    break;
                                case 2:
                                    carSearchType = CarSearchType.RENAVAM;
                                    break;

                                default:
                                    System.out.println(
                                            "Invalid search type selected. Try 1 for Name, 2 for Renavame.");
                                    continue;
                            }
                        }

                        System.out.println("Search > ");
                        String searchTerm = sc.next();

                        objectOutputStream.writeObject(carSearchType);
                        objectOutputStream.writeUTF(searchTerm);
                        objectOutputStream.flush();

                        try {
                            Car foundedUser = (Car) objectInputStream.readObject();

                            if (foundedUser != null)
                                System.out.println(foundedUser.toString());
                            else
                                System.out.println("No car found.");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                        continue;
                    case 6:
                        int availableCarsQuantity = objectInputStream.readInt();
                        System.out.println(availableCarsQuantity);
                        break;

                    case 7:

                        break;

                    case 0:

                        break;

                    default:
                        continue;
                }
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
