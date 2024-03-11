package services.application;

import java.rmi.Remote;
import java.rmi.RemoteException;

import model.Car;
import types.Types.CarSearchType;

public interface ApplicationInterface extends Remote {
    void postCar(Car newCar) throws RemoteException;

    void deleteCar(String carName) throws RemoteException;

    Car getCar(String searchTerm, CarSearchType carSearchType) throws RemoteException;

    void putCar() throws RemoteException;

    void getAllCars(CarSearchType carSearchType) throws RemoteException;

    int getCarsQuantity() throws RemoteException;

    void buyCar() throws RemoteException;
}
