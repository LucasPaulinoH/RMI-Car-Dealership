package model;

import java.io.Serializable;
import java.util.UUID;

import types.AccountType;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String login, password;
    private AccountType accountType;

    public User(String login, String password, AccountType accountType) {
        this.id = UUID.randomUUID();
        this.login = login;
        this.password = password;
        this.accountType = accountType;
    }

    public UUID getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
