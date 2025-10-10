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

        TransactionManager.saveTransaction(transaction);
        System.out.println("Deposit added successfully!");
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

        if (amount > 0) amount = -amount;

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
        System.out.println("Payment recorded successfully!");
    }
    // Ledger Method is private meaning Only this class can access it.
    // Displays all saved transactions from transactions.csv in a neat table
    private static void showLedger(Scanner scanner) {//a private static method The method reads each line from transactions.csv, splits it by pipes (|), and creates Transaction objects the Scanner parameter allows reading user input for choices.
        boolean viewingLedger = true;//Declares a loop control variable As long as this is true, the ledger screen stays active

        while (viewingLedger) {//Displays the Ledger menu options every time the loop runs.
            System.out.println("\n=========== LEDGER MENU ===========");
            System.out.println("A) All Entries");
            System.out.println("D) Deposits Only");
            System.out.println("P) Payments Only");
            System.out.println("R) Reports");
            System.out.println("0) Back");
            System.out.println("H) Home");
            System.out.println("===================================");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().trim().toUpperCase();//scanner.nextLine() waits for the user’s input.
//.trim() removes spaces and .toUpperCase() ensures input like d or D both work

            List<Transaction> transactions = TransactionManager.loadTransactions();//Calls a method from another class
            Collections.reverse(transactions); // reverses the list order so the newest transactions show first.

            switch (choice) {
                case "A":
                    displayTransactions(transactions, "All Transactions");//Shows all transactions
                    break;
                case "D":
                    displayTransactions(filterDeposits(transactions), "Deposits Only");//removes all negative amounts (leaving only deposits).
                    break;
                case "P":
                    displayTransactions(filterPayments(transactions), "Payments Only");//keeps only transactions with negative amounts (expenses).
                    break;
                case "R":
                    showreports(scanner, transactions);//Takes the user to the Reports Menu
                    break;
                case "0"://Ends the ledger loop and goes back to the previous menu.
                    viewingLedger = false;
                    break;
                case "H":// goes back to Home screen
                    return;
                default:
                    System.out.println("Invalid option, please try again.");//Handles invalid input by prompting the user to re-enter a valid choice.
            }
        }
    }
    // FILTER METHODS
    private static List<Transaction> filterDeposits(List<Transaction> transactions) {//Creates a new empty list named deposits
        List<Transaction> deposits = new ArrayList<>();//Loops through every transaction.
        for (Transaction t : transactions) {
            if (t.getAmount() > 0) deposits.add(t);//If the transaction’s amount is positive, it’s considered a deposit and added to the list.
        }
        return deposits;//Returns the list of deposits.
    }
    private static List<Transaction> filterPayments(List<Transaction> transactions) {//Same as above, but filters negative amounts (expenses or outgoing payments).
        List<Transaction> payments = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getAmount() < 0) payments.add(t);
        }
        return payments;
    }
    private static List<Transaction> filterByMonth(List<Transaction> transactions, int year, int month) {//Takes a year and month, loops through all transactions.
        List<Transaction> results = new ArrayList<>();//Converts each transaction’s stored back into a LocalDate.
        for (Transaction t : transactions) {
            LocalDate d = LocalDate.parse(t.getDate());//Checks if the year and month match the user’s selection.
            if (d.getYear() == year && d.getMonthValue() == month) results.add(t);
        }
        return results;//Returns only those transactions.
    }
    private static List<Transaction> filterByYear(List<Transaction> transactions, int year) {//Filters transactions by year only.
        List<Transaction> results = new ArrayList<>();
        for (Transaction t : transactions) {
            LocalDate d = LocalDate.parse(t.getDate());//Parses each date string to a LocalDate, checks if d.getYear() matches
            if (d.getYear() == year) results.add(t);
        }
        return results;//Returns the list for that year.
    }
    private static List<Transaction> filterByVendor(List<Transaction> transactions, String vendorName) {//Filters by vendor name.
        List<Transaction> results = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getVendor().equalsIgnoreCase(vendorName)) results.add(t);//equalsIgnoreCase() ensures case-insensitive matching.
        }
        return results;//Returns all transactions that match that vendor.
    }
    // DISPLAY METHOD
    private static void displayTransactions(List<Transaction> transactions, String title) {
        System.out.println("\n--- " + title + " ---");//Prints the title
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");//Checks if the list is empty and stops early if there’s nothing to show.
            return;
        }

        // Table-style output Prints a formatted table header using printf()
        System.out.printf("%-12s %-10s %-30s %-20s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("--------------------------------------------------------------------------------------");
//12-character column for Date
//10-character column for Time
//30-character column for Description
//20-character column for Vendor
//10-character numeric column for Amount.

        for (Transaction t : transactions) {//Loops through each transaction and prints it in the same formatted table
            System.out.printf("%-12s %-10s %-30s %-20s %-10.2f%n",
                    t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
        }
    }
    // Ensures the user enters a valid numeric value.
    private static double getValidAmount(Scanner scanner) {
        while (true) {//Reads input.
            try {
                String input = scanner.nextLine().trim();//Trims spaces.
                return Double.parseDouble(input);//Attempts to parse it to a double.
            } catch (NumberFormatException e) {//returns that number if successful
                System.out.print("Invalid number. Please enter a valid amount: ");//catches the exception and re-prompts the user if un successful
            }
        }
    }
}




