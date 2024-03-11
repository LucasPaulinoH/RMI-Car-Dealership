package communication;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import dto.LoginDTO;
import dto.RegisterDTO;
import model.Customer;
import model.Employee;
import types.AccountType;
import utils.PasswordHasher;

public class ClientThread extends Thread {
    private ObjectOutputStream objectOutputStream;
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
            while (isConnectionEstablished) {
                System.out.println("(1) - Login\n(2) - Register\n\n(0) - exit");
                System.out.print("> ");
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

                        System.out.println(employeeRole);
                        if (registerAccountType == 1) {
                            Customer newCustomer = new Customer(registerLogin, registerPassword);
                            registerDTO = new RegisterDTO(newCustomer);
                        } else {
                            Employee newEmployee = new Employee(registerLogin, registerPassword, employeeRole);
                            registerDTO = new RegisterDTO(newEmployee);
                        }

                        objectOutputStream.writeObject(registerDTO);
                        objectOutputStream.flush();

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
}
