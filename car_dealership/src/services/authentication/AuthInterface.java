package services.authentication;

import java.rmi.Remote;
import java.rmi.RemoteException;

import dto.account.LoginDTO;
import dto.account.RegisterDTO;
import model.User;

public interface AuthInterface extends Remote {
    User authenticate(LoginDTO loginDTO) throws RemoteException;
    void register(RegisterDTO registerDTO) throws RemoteException;
}
