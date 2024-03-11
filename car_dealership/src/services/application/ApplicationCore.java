package services.application;

import java.rmi.RemoteException;
import java.util.ArrayList;

import model.Car;
import types.Types.CarSearchType;

public class ApplicationCore implements ApplicationInterface {
    private ArrayList<Car> registeredCars = new ArrayList<>();
    private int totalCarQuantity = 0;

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

        System.out.println(newCar.toString() + " has been added.");
    }

    @Override
    public void deleteCar(String carName) throws RemoteException {
        Car iterableCar;
        for (int i = 0; i < registeredCars.size(); i++) {
            iterableCar = registeredCars.get(i);
            if (iterableCar.getName().equalsIgnoreCase(carName)) {
                System.out.println(iterableCar.getName() + " has been deleted.");
                registeredCars.remove(i);
            }
        }
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
    public void putCar() throws RemoteException {

    }

    @Override
    public void getAllCars(CarSearchType carSearchType) throws RemoteException {
        for (Car iterableCar : registeredCars) {
            System.out.println(iterableCar.toString());
        }
    }

    @Override
    public int getCarsQuantity() throws RemoteException {
        return totalCarQuantity;
    }

    @Override
    public void buyCar() throws RemoteException {

    }

}
