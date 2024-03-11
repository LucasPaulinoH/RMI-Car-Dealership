package model;

import types.Types.AccountType;

public class Employee extends User {
    private String role;

    public Employee(String login, String password, String role) {
        super(login, password, AccountType.EMPLOYEE);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
