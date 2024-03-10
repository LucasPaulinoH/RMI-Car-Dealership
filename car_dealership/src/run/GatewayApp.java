package run;

import connection.Gateway;
import constants.Constants;

public class GatewayApp {
    public static void main(String[] args) {
        new Gateway(Constants.GATEWAY_PORT);
    }
}
