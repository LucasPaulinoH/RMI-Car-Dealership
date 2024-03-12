package services.application;

import java.rmi.Remote;
import java.rmi.RemoteException;

import model.Car;
import types.Types.CarCategory;
import types.Types.CarSearchType;
import java.util.ArrayList;

public interface ApplicationInterface extends Remote {
    void postCar(Car newCar) throws RemoteException;

    boolean deleteCar(int deletionType, String carName) throws RemoteException;

    Car getCar(String searchTerm, CarSearchType carSearchType) throws RemoteException;

    int getCarIndex(String searchTerm, CarSearchType carSearchType) throws RemoteException;

    void putCar(Car updatedCar, int foundedCarIndex) throws RemoteException;

    ArrayList<Car> getAllCars(CarSearchType carSearchType, CarCategory carCategory) throws RemoteException;

    int getCarsQuantity() throws RemoteException;

    void buyCar() throws RemoteException;
}
