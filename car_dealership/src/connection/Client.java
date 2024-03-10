package connection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import constants.Constants;
import dto.LoginDTO;
import utils.PasswordHasher;

public class Client {
    private final Scanner sc = new Scanner(System.in);

    private int serverPort;
    private Socket serverConnectionSocket;
    private ObjectOutputStream objectOutputStream;

    public Client(int serverPort) {
        this.serverPort = serverPort;
        this.executeClientProcess();
    }

    private void executeClientProcess() {
        try {
            boolean isConnectionEstablished = false;

            while (!isConnectionEstablished) {
                try {
                    serverConnectionSocket = new Socket(Constants.IP_ADDRESS, serverPort);
                    System.out.println("Gateway connection has been established.");
                    isConnectionEstablished = true;
                } catch (IOException e) {
                    System.out.println("Gateway couldn't be reach. Retrying in 5s...");
                    Thread.sleep(5000);
                }
            }

            objectOutputStream = new ObjectOutputStream(serverConnectionSocket.getOutputStream());

            while (isConnectionEstablished) {
                System.out.println("(1) - Login\n(2) - Register\n\n(0) - exit");
                int loginOrRegister = sc.nextInt();

                objectOutputStream.writeInt(loginOrRegister);

                switch (loginOrRegister) {
                    case 1:
                        System.out.print("Login > ");
                        String login = sc.next();

                        System.out.print("Password > ");
                        String password = sc.next();
                        password = PasswordHasher.hashPassword(password);

                        var loginDTO = new LoginDTO(login, password);
                        objectOutputStream.writeObject(loginDTO);
                        objectOutputStream.flush();

                        break;
                    case 2:

                        break;
                    case 0:
                        break;
                    default:
                        System.err.println("Invalid option. Try 1 for Login, 2 for Register or 0 to Exit.");
                        continue;
                }

                break;
            }

            objectOutputStream.close();
            sc.close();
            serverConnectionSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
