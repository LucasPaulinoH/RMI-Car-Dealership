package dto;

import java.io.Serializable;

import model.User;

public class RegisterDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private User createdUser;

    public RegisterDTO(User createdUser) {
        this.createdUser = createdUser;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }
}
