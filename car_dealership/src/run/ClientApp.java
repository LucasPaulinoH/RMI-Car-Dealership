package run;

import connection.Client;
import constants.Constants;

public class ClientApp {
    public static void main(String[] args) {
        new Client(Constants.GATEWAY_PORT);
    }
}
