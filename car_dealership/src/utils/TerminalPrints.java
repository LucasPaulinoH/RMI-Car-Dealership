package utils;

public class TerminalPrints {
    public static void printLoggedEmployeeOptions() {
        System.out.println(
                "(1) - List all cars" +
                        "\n(2) - Search for car" +
                        "\n(3) - Show car quantity" +
                        "\n(4) - Buy a car" +
                        "\n(5) - Add new car" +
                        "\n(6) - Delete car" +
                        "\n(7) - Update car info" + "\n\n(0) - exit");
        System.out.print("> ");
    }

    public static void printLoggedCustomerOptions() {
        System.out.println("(1) - List all cars" +
                "\n(2) - Search for car" +
                "\n(3) - Show car quantity" +
                "\n(4) - Buy a car" + "\n\n(0) - exit");
        System.out.print("> ");
    }

    public static void printUnloggedUserOptions() {
        System.out.println("===================");
        System.out.println("(1) - Login\n(2) - Register\n\n(0) - exit");
        System.out.print("> ");
    }

    public static synchronized void clearConsole() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    public static void printDealershipLogo() {
        System.out.println("CAR DEALERSHIP\n==============\n");
    }
}
