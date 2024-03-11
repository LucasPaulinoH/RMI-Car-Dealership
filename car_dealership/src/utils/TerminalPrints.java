package utils;

public class TerminalPrints {
    public static void printLoggedEmployeeOptions() {
        System.out.println("(1) - Add new car" +
                "\n(2) - Delete car" +
                "\n(3) - Update car info" +
                "\n(4) - List all cars" +
                "\n(5) - Search for car" +
                "\n(6) - Show car quantity" +
                "\n(7) - Buy a car" + "\n\n(0) - exit");
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
        System.out.println("(1) - Login\n(2) - Register\n\n(0) - exit");
        System.out.print("> ");
    }
}
