package com.pluralsight;

import java.util.*;//Reading user input such as (Scanner) and Handling lists of transactions (objects)
import java.time.*;//Getting the current date and current time
import java.time.format.DateTimeFormatter;//Imports the class thus allows me to format time or date into a custom pattern


public class Main {//Declares your main class, named Main.
    public static void main(String[] args) {//Defines the main method that will be called when my Java program starts.
        Scanner scanner = new Scanner(System.in);//Creates a new Scanner object for keyboard input
        boolean running = true;//a boolean flag that controls the main loop so When running becomes false, the program exits the loop and stops.

        while (running) {//a loop that keeps showing the menu and waiting for user input until the user chooses to exit
            System.out.println("\n--- Financial Tracker ---");//Prints my program’s menu on the console , \n adds a blank line
            System.out.println("1. Add Transaction");
            System.out.println("2. View Transactions");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");//( print() ) = (no newline) which keeps the cursor on the same line for user input.
            String choice = scanner.nextLine();//Reads the user’s input

            switch (choice) {//(A switch statement) checks the user’s input and decides which option to execute.
                case "1":
                    addTransaction(scanner);//If the user typed "1", it calls the addTransaction(scanner) method etc
                    break;//The (break;) stops further cases from running
                case "2":
                    viewTransactions();
                    break;
                case "3":
                    running = false;//Sets running to false, which ends the while loop
                    System.out.println("Exiting. Goodbye!");//Prints a friendly exit message before the program stops.
                    break;
                default://if the user types anything other than "1", "2", or "3".
                    System.out.println("Invalid choice, please try again.");//Tells them to enter a valid option.
            }
        }
    }
//Declares a private meaning it is only used inside this class
    private static void addTransaction(Scanner scanner) {//handles creating a new transaction from user input and saving it to my CSV file
        System.out.print("Description: ");//Prompts the user for each piece of transaction info.
        String description = scanner.nextLine();//Reads what they type in.
        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Amount (negative for expenses, positive for income): ");
        double amount = Double.parseDouble(scanner.nextLine());

        LocalDate date = LocalDate.now();//gets today’s date
        LocalTime time = LocalTime.now();//gets current time

        Transaction transaction = new Transaction(//Calls the Transaction constructor to create a new transaction
                date.toString(),//Converts date to a String
                time.format(DateTimeFormatter.ofPattern("HH:mm:ss")),//Formats time by giving the hours:minutes:seconds.
                description,//Passes all values (date, time, description, vendor, amount) into the object.
                vendor,
                amount
        );

        TransactionManager.saveTransaction(transaction);//Calls on my TransactionManager class’s static method
    }

    private static void viewTransactions() {//a method that reads all saved transactions from the CSV file and prints them and Declares another private static helper method.
        List<Transaction> transactions = TransactionManager.loadTransactions();//Calls the loadTransactions methods in TransactionManager
        if (transactions.isEmpty()) {//If there are no transactions, tells the user the file is empty.
            System.out.println("No transactions found.");
        } else {//Loops through each Transaction object in the list
            System.out.println("\n--- Transaction List ---");//calls the Transaction class’s toString() method automatically:
            for (Transaction t : transactions) {
                System.out.println(t);
            }
        }
    }
}


