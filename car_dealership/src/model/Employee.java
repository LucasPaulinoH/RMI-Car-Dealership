package model;

public class Employee extends User {
    private String role;

    public Employee(String login, String password, String fullname, String cpf, String phone, String role) {
        super(login, password, fullname, cpf, phone);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
