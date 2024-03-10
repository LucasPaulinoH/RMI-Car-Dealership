package services.authentication;

import java.rmi.RemoteException;

public class AuthCore implements AuthInterface {
    @Override
    public void authenticate(String login, String password) throws RemoteException {
        System.out.println("Login: " + login);
        System.out.println("Password " + password);
    }
}
