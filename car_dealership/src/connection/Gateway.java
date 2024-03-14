package connection;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import communication.gateway.GatewayThread;
import constants.Constants;
import services.authentication.AuthInterface;
import services.authentication.AuthService;

public class Gateway {
    private ServerSocket serverSocket;
    private Socket clientConnectionSocket;

    private ObjectInputStream objectInputStream;

    private int serverPort;

    public Gateway(int serverPort) {
        this.serverPort = serverPort;
        this.executeGatewayProcess();
    }

    private void executeGatewayProcess() {
        try {

            try {
                serverSocket = new ServerSocket(serverPort);
                System.out.println("Gateway is running. Waiting for client connections...");

                while (true) {
                    clientConnectionSocket = serverSocket.accept();
                    System.out.println("A client has connected.");

                    objectInputStream = new ObjectInputStream(clientConnectionSocket.getInputStream());

                    var gatewayThread = new GatewayThread(objectInputStream,  clientConnectionSocket);
                    gatewayThread.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
