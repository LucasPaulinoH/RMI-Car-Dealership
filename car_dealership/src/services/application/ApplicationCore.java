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

    public ApplicationCore() throws RemoteException {
        this.postCar(new Car("Lancer", CarCategory.INTERMEDIATE, "12345678910", "2016", 60000));
        this.postCar(new Car("Civic", CarCategory.INTERMEDIATE, "12345678911", "2018", 80000));
        this.postCar(new Car("Corolla", CarCategory.INTERMEDIATE, "12345678912", "2017", 75000));
        this.postCar(new Car("Gol", CarCategory.ECONOMIC, "12345678913", "2019", 45000));
        this.postCar(new Car("Onix", CarCategory.ECONOMIC, "12345678914", "2020", 50000));
        this.postCar(new Car("Fusion", CarCategory.EXECUTIVE, "12345678915", "2019", 100000));
        this.postCar(new Car("Camry", CarCategory.EXECUTIVE, "12345678916", "2021", 120000));
        this.postCar(new Car("Focus", CarCategory.INTERMEDIATE, "12345678917", "2018", 70000));
        this.postCar(new Car("Fit", CarCategory.ECONOMIC, "12345678918", "2017", 55000));
        this.postCar(new Car("Sentra", CarCategory.INTERMEDIATE, "12345678919", "2018", 75000));
        this.postCar(new Car("A4", CarCategory.EXECUTIVE, "12345678920", "2019", 90000));
        this.postCar(new Car("Fiesta", CarCategory.ECONOMIC, "12345678921", "2020", 48000));
    }

    @Override
    public synchronized Car postCar(Car newCar) throws RemoteException {
        for (int i = 0; i < registeredCars.size(); i++) {
            if (newCar.getRenavam().equalsIgnoreCase(registeredCars.get(i).getRenavam())) {
                return null;
            }
        }

        registeredCars.add(newCar);
        System.out.println(newCar.getName() + " has been added.");

        return newCar;
    }

    @Override
    public synchronized boolean deleteCar(CarSearchType deleteType, String searchTerm) throws RemoteException {
        ArrayList<Car> foundedCars = getCar(searchTerm, deleteType);

        if (foundedCars.size() == 0)
            return false;

        Car iterableFoundedCar = null;
        Car iterableRegisteredCar = null;

        for (int i = 0; i < foundedCars.size(); i++) {
            iterableFoundedCar = foundedCars.get(i);
            for (int j = 0; j < registeredCars.size(); j++) {
                iterableRegisteredCar = registeredCars.get(j);

                if (iterableFoundedCar.getRenavam().equalsIgnoreCase(iterableRegisteredCar.getRenavam())) {
                    registeredCars.remove(j);
                    break;
                }
            }
        }

        return true;
    }

    @Override
    public ArrayList<Car> getCar(String searchTerm, CarSearchType carSearchType) throws RemoteException {
        ArrayList<Car> foundedCars = new ArrayList<>();

        for (Car iterableCar : registeredCars) {
            switch (carSearchType) {
                case NAME:
                    if (iterableCar.getName().equalsIgnoreCase(searchTerm))
                        foundedCars.add(iterableCar);
                case RENAVAM:
                    if (iterableCar.getRenavam().equalsIgnoreCase(searchTerm))
                        foundedCars.add(iterableCar);
            }
        }

        return foundedCars;
    }

    @Override
    public synchronized boolean putCar(Car updatedCar, int foundedCarIndex) throws RemoteException {
        Car iterableCar = null;
        for (int i = 0; i < registeredCars.size(); i++) {
            iterableCar = registeredCars.get(i);

            if (iterableCar.getRenavam().equalsIgnoreCase(updatedCar.getRenavam()))
                return false;
        }

        registeredCars.set(foundedCarIndex, updatedCar);

        System.out.println(foundedCarIndex + "(index) car sucessfully updated.");
        return true;
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
        return registeredCars.size();
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
    public int getCarIndexFromRenavam(String searchedRenavam) throws RemoteException {
        int foundedCarIndex = -1;

        for (int i = 0; i < registeredCars.size(); i++) {
            if (registeredCars.get(i).getRenavam().equalsIgnoreCase(searchedRenavam)) {
                foundedCarIndex = i;
                break;
            }
        }

        return foundedCarIndex;
    }
}
