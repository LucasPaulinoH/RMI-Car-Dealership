package services.application;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import constants.Constants;

public class ApplicationService {
    public ApplicationService() {
        this.main(null);
    }

    public static void main(String[] args) {
        try {
            var remoteDatabaseRef = new ApplicationCore();
            var skeleton = (ApplicationInterface) UnicastRemoteObject.exportObject(remoteDatabaseRef, 0);

            LocateRegistry.createRegistry(Constants.APP_REGISTRY_PORT);
            Registry registry = LocateRegistry.getRegistry(Constants.APP_REGISTRY_PORT);

            registry.bind(Constants.APP_SERVICE_NAME, skeleton);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
