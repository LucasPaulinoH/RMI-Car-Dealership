package connection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import communication.ClientThread;
import constants.Constants;

public class Client {
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

            var clientThread = new ClientThread(objectOutputStream, serverConnectionSocket);
            clientThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
