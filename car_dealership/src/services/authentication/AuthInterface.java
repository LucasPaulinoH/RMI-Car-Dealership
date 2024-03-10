package services.authentication;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthInterface extends Remote {
    void authenticate(String login, String password) throws RemoteException;
}
