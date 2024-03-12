package communication.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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
        this.executeLoggedUserProcess();
    }

    private void executeLoggedUserProcess() {
        try {
            while (true) {
                if (accountType == AccountType.CUSTOMER)
                    TerminalPrints.printLoggedCustomerOptions();
                else
                    TerminalPrints.printLoggedEmployeeOptions();

                int selectedOption = sc.nextInt();

                objectOutputStream.writeInt(selectedOption);
                objectOutputStream.flush();

                TerminalPrints.clearConsole();
                switch (selectedOption) {
                    case 1:
                        int carListSearchTypeInt;
                        CarSearchType carListSearchType = null;
                        CarCategory searchCategory = null;

                        while (carListSearchType == null) {
                            System.out.println("Search type (1 - general, 2 - by category) > ");
                            carListSearchTypeInt = sc.nextInt();

                            switch (carListSearchTypeInt) {
                                case 1:
                                    carListSearchType = CarSearchType.GENERAL;
                                    break;
                                case 2:
                                    carListSearchType = CarSearchType.CATEGORY;

                                    while (searchCategory == null) {
                                        System.out
                                                .println("Category (1 - economic, 2 - intermediate, 3 - executive) > ");
                                        carListSearchTypeInt = sc.nextInt();

                                        switch (carListSearchTypeInt) {
                                            case 1:
                                                searchCategory = CarCategory.ECONOMIC;
                                                break;
                                            case 2:
                                                searchCategory = CarCategory.INTERMEDIATE;
                                                break;
                                            case 3:
                                                searchCategory = CarCategory.EXECUTIVE;
                                                break;
                                            default:
                                                System.out.println(
                                                        "Invalid category selected. Try 1 for Economic, 2 for Intermediate and 3 for Executive.");
                                                continue;
                                        }
                                    }
                                    break;
                                default:
                                    System.out.println(
                                            "Invalid search type selected. Try 1 for Name, 2 for Renavame.");
                                    continue;
                            }
                        }

                        objectOutputStream.writeObject(carListSearchType);
                        objectOutputStream.writeObject(searchCategory);
                        objectOutputStream.flush();

                        ArrayList<Car> searchedCars = (ArrayList<Car>) objectInputStream.readObject();

                        if (searchedCars.size() == 0) {
                            System.out.println("Car list is empty.");
                        } else {
                            for (Car iterableCar : searchedCars) {
                                System.out.println(iterableCar.toString());
                            }
                        }

                        continue;
                    case 2:
                        searchCarRequest(objectOutputStream);

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
                    case 3:

                        int availableCarsQuantity = objectInputStream.readInt();
                        System.out.println("Available cars: " + availableCarsQuantity);
                        continue;
                    case 4:

                        break;
                    case 5:
                        if (accountType == AccountType.EMPLOYEE) {
                            Car newCar = addCarRequest();

                            objectOutputStream.writeObject(newCar);
                            objectOutputStream.flush();

                            System.out.println(newCar.getName() + " successfully added.");
                            continue;
                        } else {
                            System.out.println("Invalid option selected.");
                            continue;
                        }
                    case 6:
                        if (accountType == AccountType.EMPLOYEE) {
                            deleteCarRequest(objectOutputStream);

                            boolean hasDeleted = (boolean) objectInputStream.readBoolean();

                            if (hasDeleted)
                                System.out.println("Successfully deleted.");
                            else
                                System.out.println("Given car is not registered.");

                            continue;
                        } else {
                            System.out.println("Invalid option selected.");
                            continue;
                        }
                    case 7:
                        if (accountType == AccountType.EMPLOYEE) {
                            searchCarRequest(objectOutputStream);

                            int foundedCarIndex = objectInputStream.readInt();

                            if (foundedCarIndex == -1) {
                                System.out.println("Searched car is not registered.");
                                continue;
                            }

                            Car updatedCar = addCarRequest();
                            objectOutputStream.writeObject(updatedCar);
                            objectOutputStream.flush();
                        } else {
                            System.out.println("Invalid option selected.");
                            continue;
                        }

                        continue;

                    case 0:
                        break;
                    default:
                        System.out.println("Invalid option selected.");
                        continue;
                }
                break;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private Car addCarRequest() {
        System.out.print("Car name > ");
        String newCarName = sc.next();

        int categoryInt;
        CarCategory category = null;
        while (category == null) {
            System.out.print("Category (1 - economic, 2 - intermediate, 3 - executive) > ");
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

        System.out.print("Renavam > ");
        String renavam = sc.next();

        System.out.println("Manufacture year > ");
        String manufactureYear = sc.next();

        System.out.println("Price (R$) > ");
        double price = sc.nextDouble();

        System.out.println("Quantity > ");
        int quantity = sc.nextInt();



        return new Car(newCarName, category, renavam, manufactureYear, price, quantity);
    }

    private void deleteCarRequest(ObjectOutputStream objectOutputStream) {
        int deletionType;

        while (true) {
            System.out.println("Deletion type (1 - unity, 2 - all unities) > ");
            deletionType = sc.nextInt();

            switch (deletionType) {
                case 1:
                    break;
                case 2:
                    break;
                default:
                    System.out.println(
                            "Invalid deletion type. Type 1 to delete a single instance or 2 to delete all registries.");
                    continue;
            }

            break;
        }

        System.out.print("Car name > ");
        String deletedCarName = sc.next();

        try {
            objectOutputStream.writeInt(deletionType);
            objectOutputStream.writeUTF(deletedCarName);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void searchCarRequest(ObjectOutputStream objectOutputStream) {
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

        System.out.print("Search > ");
        String searchTerm = sc.next();

        try {
            objectOutputStream.writeObject(carSearchType);
            objectOutputStream.writeUTF(searchTerm);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
