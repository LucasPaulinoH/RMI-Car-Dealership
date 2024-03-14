package services.application;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import model.Car;
import types.Types.CarCategory;
import types.Types.CarSearchType;
import java.util.UUID;

public class ApplicationCore implements ApplicationInterface {
    private ArrayList<Car> registeredCars = new ArrayList<>();

    private int totalCarQuantity = 0;

    public ApplicationCore() throws RemoteException {
        this.postCar(new Car("Lancer", CarCategory.INTERMEDIATE, "12345678910", "2016", 60000, 2));
        this.postCar(new Car("Civic", CarCategory.INTERMEDIATE, "12345678911", "2018", 80000, 3));
        this.postCar(new Car("Corolla", CarCategory.INTERMEDIATE, "12345678912", "2017", 75000, 1));
        this.postCar(new Car("Gol", CarCategory.ECONOMIC, "12345678913", "2019", 45000, 5));
        this.postCar(new Car("Onix", CarCategory.ECONOMIC, "12345678914", "2020", 50000, 4));
        this.postCar(new Car("Fusion", CarCategory.EXECUTIVE, "12345678915", "2019", 100000, 2));
        this.postCar(new Car("Camry", CarCategory.EXECUTIVE, "12345678916", "2021", 120000, 1));
        this.postCar(new Car("Focus", CarCategory.INTERMEDIATE, "12345678917", "2018", 70000, 3));
        this.postCar(new Car("Fit", CarCategory.ECONOMIC, "12345678918", "2017", 55000, 2));
        this.postCar(new Car("Sentra", CarCategory.INTERMEDIATE, "12345678919", "2018", 75000, 2));
        this.postCar(new Car("A4", CarCategory.EXECUTIVE, "12345678920", "2019", 90000, 1));
        this.postCar(new Car("Fiesta", CarCategory.ECONOMIC, "12345678921", "2020", 48000, 3));
    }

    @Override
    public synchronized void postCar(Car newCar) throws RemoteException {
        boolean hasPostedCar = false;
        for (Car iterableCar : registeredCars) {
            if (newCar.getName().equalsIgnoreCase(iterableCar.getName())
                    && newCar.getRenavam().equalsIgnoreCase(iterableCar.getRenavam())) {
                iterableCar.setQuantity(iterableCar.getQuantity() + newCar.getQuantity());

                totalCarQuantity += newCar.getQuantity();
                hasPostedCar = true;
                break;
            }
        }

        if (!hasPostedCar) {
            registeredCars.add(newCar);
            totalCarQuantity += newCar.getQuantity();
        }

        System.out.println(newCar.getName() + " has been added.");
    }

    @Override
    public synchronized boolean deleteCar(int deletionType, String carName) throws RemoteException {
        Car iterableCar = null;
        for (int i = 0; i < registeredCars.size(); i++) {
            iterableCar = registeredCars.get(i);

            if (iterableCar.getName().equalsIgnoreCase(carName)) {
                if (deletionType == 1) {
                    iterableCar.setQuantity(iterableCar.getQuantity() - 1);
                    if (iterableCar.getQuantity() == 0)
                        registeredCars.remove(i);

                    totalCarQuantity--;

                } else {
                    totalCarQuantity -= iterableCar.getQuantity();
                    registeredCars.remove(i);
                }

                System.out.println(carName + " successfully deleted.");
                return true;
            }
        }

        System.out.println(carName + " wasn't found.");
        return false;
    }

    @Override
    public Car getCar(String searchTerm, CarSearchType carSearchType) throws RemoteException {
        for (Car iterableCar : registeredCars) {
            switch (carSearchType) {
                case NAME:
                    if (iterableCar.getName().equalsIgnoreCase(searchTerm))
                        return iterableCar;
                case RENAVAM:
                    if (iterableCar.getRenavam().equalsIgnoreCase(searchTerm))
                        return iterableCar;
            }
        }

        return null;
    }

    @Override
    public synchronized void putCar(Car updatedCar, int foundedCarIndex) throws RemoteException {
        totalCarQuantity -= registeredCars.get(foundedCarIndex).getQuantity();
        registeredCars.set(foundedCarIndex, updatedCar);

        totalCarQuantity += updatedCar.getQuantity();

        System.out.println(foundedCarIndex + "(index) car sucessfully updated.");
    }

    @Override
    public ArrayList<Car> getAllCars(CarSearchType carSearchType, CarCategory carCategory) throws RemoteException {
        ArrayList<Car> filteredCars = null;
        sortCarListAlphabetically(registeredCars);
        
        switch (carSearchType) {
            case GENERAL:
                filteredCars = registeredCars;
                break;

            case CATEGORY:
                switch (carCategory) {
                    case ECONOMIC:
                        filteredCars = getEspecificCategoryCarsFromList(registeredCars, CarCategory.ECONOMIC);
                        break;
                    case INTERMEDIATE:
                        filteredCars = getEspecificCategoryCarsFromList(registeredCars, CarCategory.INTERMEDIATE);
                        break;
                    case EXECUTIVE:
                        filteredCars = getEspecificCategoryCarsFromList(registeredCars, CarCategory.EXECUTIVE);
                        break;
                }
                break;
        }

        return filteredCars;
    }

    @Override
    public int getCarsQuantity() throws RemoteException {
        return totalCarQuantity;
    }

    @Override
    public synchronized Car buyCar(UUID userId, String buyCarSearchTerm, CarSearchType buyCarSearchType)
            throws RemoteException {
        Car purchasedCar = null;
        Car iterableCar = null;

        for (int i = 0; i < registeredCars.size(); i++) {
            iterableCar = registeredCars.get(i);

            if (iterableCar.getName().equalsIgnoreCase(buyCarSearchTerm)
                    || iterableCar.getRenavam().equalsIgnoreCase(buyCarSearchTerm)) {
                iterableCar.setQuantity(iterableCar.getQuantity() - 1);

                if (iterableCar.getQuantity() == 0)
                    registeredCars.remove(i);

                purchasedCar = iterableCar;
                totalCarQuantity--;
                System.out.println(userId + " has purchased a " + purchasedCar.getName());
                return purchasedCar;
            }
        }

        return null;
    }

    private void sortCarListAlphabetically(ArrayList<Car> carList) {
        Collections.sort(carList, new Comparator<Car>() {
            @Override
            public int compare(Car car1, Car car2) {
                return car1.getName().toUpperCase().compareTo(car2.getName().toUpperCase());
            }
        });
    }

    private ArrayList<Car> getEspecificCategoryCarsFromList(ArrayList<Car> carList, CarCategory carCategory) {
        ArrayList<Car> resultingList = new ArrayList<>();

        for (Car iterableCar : carList) {
            if (iterableCar.getCategory() == carCategory)
                resultingList.add(iterableCar);
        }

        return resultingList;
    }

    @Override
    public int getCarIndex(String searchTerm, CarSearchType carSearchType) throws RemoteException {
        int foundedCarIndex = -1;

        for (int i = 0; i < registeredCars.size(); i++) {
            switch (carSearchType) {
                case NAME:
                    if (registeredCars.get(i).getName().equalsIgnoreCase(searchTerm)) {
                        foundedCarIndex = i;
                        break;
                    }

                case RENAVAM:
                    if (registeredCars.get(i).getName().equalsIgnoreCase(searchTerm)) {
                        foundedCarIndex = i;
                        break;
                    }
            }
        }

        return foundedCarIndex;
    }
}
