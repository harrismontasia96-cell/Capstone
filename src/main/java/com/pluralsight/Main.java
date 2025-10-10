package com.pluralsight;

import java.util.*;//Reading user input such as (Scanner) and Handling lists of transactions (objects)
import java.time.*;//Getting the current date and current time
import java.time.format.DateTimeFormatter;//Imports the class thus allows me to format time or date into a custom pattern


public class Main {//Declares your main class, named Main.
    public static void main(String[] args) {//Defines the main method that will be called when my Java program starts.
        Scanner scanner = new Scanner(System.in);//Creates a new Scanner object for keyboard input
        boolean running = true;//a boolean flag that controls the main loop so When running becomes false, the program exits the loop and stops

        System.out.println("========================================");
        System.out.println("    Welcome to Financial Tracker ");//Display of a header to make the app look polished when it starts.
        System.out.println("========================================");


        while (running) {//a loop that keeps showing the menu and waiting for user input until the user chooses to exit
            showHomeScreen(); // Displays the available options
            System.out.print("\nSelect an option: ");
            String choice = scanner.nextLine().trim().toUpperCase(); // Reads and normalizes  user input

            switch (choice) { // Chooses what to do based on the user’s input
                case "D":
                    addDeposit(scanner); //Calls addDeposit(scanner)
                    break;
                case "P":
                    makePayment(scanner); // Record a payment
                    break;
                case "L":
                    showLedger(); // View all transactions
                    break;
                case "X":
                    running = false; // Ends the loop and exits
                    System.out.println("\nExiting application. Goodbye!");
                    break;
                default:
                    System.out.println(" Invalid option. Please choose D, P, L, or X.");
            }
        }
    }
    // Displays the Home Screen menu
    private static void showHomeScreen() {//A helper method that prints out the main menu each time.
        System.out.println("\n=========== MAIN MENU ===========");
        System.out.println("D) Add Deposit");
        System.out.println("P) Make Payment (Debit)");
        System.out.println("L) Ledger");
        System.out.println("X) Exit");
        System.out.println("=================================");
    }
    // Add Deposit Method
    // Prompts the user for deposit details and saves them to the CSV file
    private static void addDeposit(Scanner scanner) {
        System.out.println("\n--- Add Deposit ---");
        System.out.print("Enter description: ");//what the deposit is for
        String description = scanner.nextLine();
        System.out.print("Enter vendor/source: ");//where it came from
        String vendor = scanner.nextLine();
        System.out.print("Enter deposit amount: ");
        double amount = getValidAmount(scanner); // Uses a helper method for safe input ensuring that the user enters a valid number


        LocalDate date = LocalDate.now();//gets today’s date
        LocalTime time = LocalTime.now();//gets current time

        Transaction transaction = new Transaction(//Calls the Transaction constructor to create a new transaction
                date.toString(),//Converts date to a String
                time.format(DateTimeFormatter.ofPattern("HH:mm:ss")),//Formats time by giving the hours:minutes:seconds.
                description,//Passes all values (date, time, description, vendor, amount) into the object.
                vendor,
                amount
        );

        TransactionManager.saveTransaction(addDeposit(););//Calls on my TransactionManager class’s static method
    }
    // Make Payment (Debit) Method
    // Prompts the user for payment details and saves them as a negative to indicate money leaving your account.
    private static void makePayment(Scanner scanner) {
        System.out.println("\n--- Make Payment (Debit) ---");
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Enter payment amount: ");
        double amount = getValidAmount(scanner);

        if (amount > 0) {
            amount = -amount;
        }
        LocalDate date = LocalDate.now();//Gets the current date and time
        LocalTime time = LocalTime.now();

        // Creates a Transaction object for the payment and adds a new line in the CSV file.
        Transaction payment = new Transaction(
                date.toString(),
                time.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                description,
                vendor,
                amount
        );
        // Saves it to the CSV file
        TransactionManager.saveTransaction(payment);
    }
    // Ledger Method
    // Displays all saved transactions from transactions.csv in a neat table
    private static void showLedger() {//The method reads each line from transactions.csv, splits it by pipes (|), and creates Transaction objects.
        System.out.println("\n--- Ledger ---");
        List<Transaction> transactions = TransactionManager.loadTransactions();

        if (transactions.isEmpty()) {//If there are no saved transactions, print a message and stop.
            System.out.println("No transactions found.");
            return;
        }
        // Table-style output Prints a formatted table header using printf()
        System.out.printf("%-12s %-10s %-30s %-20s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("--------------------------------------------------------------------------------------");

        for (Transaction t : transactions) {//Loops through each transaction and prints it in the same formatted table
            System.out.printf("%-12s %-10s %-30s %-20s %-10.2f%n",
                    t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
        }
    }
    // Helper Method for Amount Input Validation
    private static double getValidAmount(Scanner scanner) {//Keeps asking until the user types a valid number
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {//If it fails, NumberFormatException triggers and asks the user again.
                System.out.print("Invalid number. Please enter a valid amount: ");
            }
        }
    }
}




