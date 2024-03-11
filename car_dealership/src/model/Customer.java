package model;

import types.AccountType;

public class Customer extends User {
    public Customer(String login, String password) {
        super(login, password, AccountType.CUSTOMER);
    }
}
