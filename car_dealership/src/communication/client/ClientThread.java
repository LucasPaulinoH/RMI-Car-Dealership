package communication.client;

import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

import dto.account.LoginDTO;
import dto.account.RegisterDTO;
import model.Customer;
import model.Employee;
import model.User;
import types.Types.AccountType;
import utils.PasswordHasher;
import utils.TerminalPrints;

public class ClientThread extends Thread {
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private Socket serverConnectionSocket;

    private boolean isConnectionEstablished = true;

    private Scanner sc = new Scanner(System.in);

    public ClientThread(ObjectOutputStream objectOutputStream, Socket serverConnectionSocket) {
        this.objectOutputStream = objectOutputStream;
        this.serverConnectionSocket = serverConnectionSocket;
    }

    @Override
    public void run() {
        try {
            objectInputStream = new ObjectInputStream(serverConnectionSocket.getInputStream());

            while (isConnectionEstablished) {
                TerminalPrints.printUnloggedUserOptions();
                int loginOrRegister = sc.nextInt();

                objectOutputStream.writeInt(loginOrRegister);

                TerminalPrints.clearConsole();
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

                        User loggedUser = (User) objectInputStream.readObject();

                        if (loggedUser == null) {
                            System.out.println("Invalid login credentials.");
                            continue;
                        }

                        TerminalPrints.clearConsole();

                        LoggedUserProcess loggedUserProcess;
                        if (loggedUser instanceof Employee) {
                            loggedUserProcess = new LoggedUserProcess(loggedUser, AccountType.EMPLOYEE,
                                    objectInputStream,
                                    objectOutputStream);
                        } else {
                            loggedUserProcess = new LoggedUserProcess(loggedUser, AccountType.CUSTOMER,
                                    objectInputStream,
                                    objectOutputStream);
                        }
                        break;
                    case 2:
                        createRegisterRequest();
                        break;
                    case 0:
                        isConnectionEstablished = false;
                        break;
                    default:
                        System.err.println("Invalid option. Try 1 for Login, 2 for Register or 0 to Exit.");
                        continue;
                }
            }

            sc.close();
            objectOutputStream.close();
            serverConnectionSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createRegisterRequest() {
        System.out.print("Create a login > ");
        String registerLogin = sc.next();

        int registerAccountType;
        String employeeRole = "";

        while (true) {
            System.out.print("Account type (1) - Customer | (2) - Employee > ");
            registerAccountType = sc.nextInt();

            if (registerAccountType == 1) {
                break;
            } else if (registerAccountType == 2) {
                System.out.print("Employee role > ");
                employeeRole = sc.next();

                break;
            } else {
                System.out.println("Invalid account type. Type 1 to Customer or 2 to Employee.");
                continue;
            }
        }

        System.out.print("Enter a password > ");
        String registerPassword = sc.next();
        registerPassword = PasswordHasher.hashPassword(registerPassword);

        RegisterDTO registerDTO;

        if (registerAccountType == 1) {
            Customer newCustomer = new Customer(registerLogin, registerPassword);
            registerDTO = new RegisterDTO(newCustomer);
        } else {
            Employee newEmployee = new Employee(registerLogin, registerPassword, employeeRole);
            registerDTO = new RegisterDTO(newEmployee);
        }

        try {
            objectOutputStream.writeObject(registerDTO);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
