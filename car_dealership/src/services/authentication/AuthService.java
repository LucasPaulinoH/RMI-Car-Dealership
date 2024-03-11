package services.authentication;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import constants.Constants;

public class AuthService {
    public AuthService() {
        this.main(null);
    }

    public static void main(String[] args) {
        try {
            var remoteDatabaseRef = new AuthCore();
            var skeleton = (AuthInterface) UnicastRemoteObject.exportObject(remoteDatabaseRef, 0);

            LocateRegistry.createRegistry(Constants.AUTH_REGISTRY_PORT);
            Registry registry = LocateRegistry.getRegistry(Constants.AUTH_REGISTRY_PORT);

            registry.bind(Constants.AUTH_SERVICE_NAME, skeleton);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
