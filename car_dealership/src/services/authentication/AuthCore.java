package services.authentication;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dto.account.LoginDTO;
import dto.account.RegisterDTO;
import model.Employee;
import model.User;

public class AuthCore implements AuthInterface {
    private ArrayList<User> registeredUsers = new ArrayList<>();

    public AuthCore() {
        this.registeredUsers.add(new Employee("lucas",
                "77+9Ou+/ve+/ve+/vW/Fhe+/ve+/vRgS77+977+9PO+/vWHvv73vv71kPu+/ve+/vT/vv70Qa2I3Je+/vWc=", "seller"));
    }

    @Override
    public User authenticate(LoginDTO loginDTO) throws RemoteException {
        User iterableUser;

        for (int i = 0; i < registeredUsers.size(); i++) {
            iterableUser = registeredUsers.get(i);

            if (loginDTO.getLogin().equals(iterableUser.getLogin())
                    && loginDTO.getPassword().equals(iterableUser.getPassword())) {
                System.out.println(iterableUser.getId() + " successfully logged in.");
                return iterableUser;
            }
        }

        System.out.println("Invalid login credentials.");
        return null;
    }

    @Override
    public void register(RegisterDTO registerDTO) throws RemoteException {
        User createdUser = registerDTO.getCreatedUser();
        registeredUsers.add(createdUser);

        if (createdUser instanceof Employee) {
            Employee employee = (Employee) createdUser;
            System.out.println(createdUser.getId() + " (" + employee.getRole() + ") successfully created.");
        } else {
            System.out.println(createdUser.getId() + " successfully created.");
        }
    }
}
