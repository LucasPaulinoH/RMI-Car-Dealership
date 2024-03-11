package communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import dto.LoginDTO;
import dto.RegisterDTO;
import services.authentication.AuthInterface;

public class GatewayThread extends Thread {
    private ObjectInputStream objectInputStream;
    private Socket clientConnectionSocket;

    private AuthInterface remoteClientStub;

    public GatewayThread(ObjectInputStream objectInputStream, AuthInterface remoteClientStub,
            Socket clientConnectionSocket) {
        this.objectInputStream = objectInputStream;
        this.remoteClientStub = remoteClientStub;
        this.clientConnectionSocket = clientConnectionSocket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int loginOrRegisterSelected = objectInputStream.readInt();

                switch (loginOrRegisterSelected) {
                    case 1:
                        LoginDTO loginDTO = (LoginDTO) objectInputStream.readObject();
                        remoteClientStub.authenticate(loginDTO);
                        break;
                    case 2:
                        RegisterDTO registerDTO = (RegisterDTO) objectInputStream.readObject();
                        remoteClientStub.register(registerDTO);
                        break;
                    case 0:
                        objectInputStream.close();
                        clientConnectionSocket.close();
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
