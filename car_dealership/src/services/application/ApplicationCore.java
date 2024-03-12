package services.application;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import model.Car;
import types.Types.CarCategory;
import types.Types.CarSearchType;

public class ApplicationCore implements ApplicationInterface {
    private ArrayList<Car> registeredCars = new ArrayList<>();
    private int totalCarQuantity = 0;

    public ApplicationCore() throws RemoteException {
        this.postCar(new Car("Lancer", CarCategory.INTERMEDIATE, "12345678910", "2016", 60000, 2));
    }

    @Override
    public void postCar(Car newCar) throws RemoteException {
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
    public boolean deleteCar(int deletionType, String carName) throws RemoteException {
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
    public void putCar(Car updatedCar, int foundedCarIndex) throws RemoteException {
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
    public void buyCar() throws RemoteException {

    }

    private void sortCarListAlphabetically(ArrayList<Car> carList) {
        Collections.sort(carList, new Comparator<Car>() {
            @Override
            public int compare(Car car1, Car car2) {
                return car1.getName().compareTo(car2.getName());
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
