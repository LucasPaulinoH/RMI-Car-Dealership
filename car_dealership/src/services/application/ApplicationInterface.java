package services.application;

import java.rmi.Remote;
import java.rmi.RemoteException;

import model.Car;
import types.Types.CarCategory;
import types.Types.CarSearchType;
import java.util.ArrayList;

public interface ApplicationInterface extends Remote {
    public Car postCar(Car newCar) throws RemoteException;

    public boolean deleteCar(CarSearchType deleteType, String searchTerm) throws RemoteException;

    public ArrayList<Car> getCar(String searchTerm, CarSearchType carSearchType) throws RemoteException;

    public int getCarIndexFromRenavam(String searchedRenavam) throws RemoteException;

    public boolean putCar(Car updatedCar, int foundedCarIndex) throws RemoteException;

    public ArrayList<Car> getAllCars(CarSearchType carSearchType, CarCategory carCategory) throws RemoteException;

    public int getCarsQuantity() throws RemoteException;

    public boolean buyCar(String searchedRenavam) throws RemoteException;
}
