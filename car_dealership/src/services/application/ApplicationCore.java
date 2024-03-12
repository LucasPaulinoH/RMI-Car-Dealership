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
    public boolean deleteCar(int deletionType, String carName) throws RemoteException {
        Car iterableCar = null;
        for (int i = 0; i < registeredCars.size(); i++) {
            iterableCar = registeredCars.get(i);

            if (iterableCar.getName().equalsIgnoreCase(carName)) {
                if (deletionType == 1) {
                    iterableCar.setQuantity(iterableCar.getQuantity() - 1);
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
