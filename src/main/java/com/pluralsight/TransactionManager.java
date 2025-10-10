package com.pluralsight;

import java.io.*;//Imports all the classes in the package (for Input/Output operations)These classes handle reading/writing text files.
import java.util.*;//Imports all utility classes like list and arraylist

public class TransactionManager {//Defines a public class

    private static final String FILE_NAME = "transactions.csv";//Declares a constant variable that stores the filename of my transaction file.

    public static void saveTransaction(Transaction transaction) {//a static method that takes a Transaction object and writes it to the CSV file.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {//try-with-resources statement
            writer.write(transaction.toCSV());//Calls  the CSV method from my Transaction class
            writer.newLine();
            System.out.println("Transaction saved successfully!");//Prints a confirmation message to the console so I know it worked.
        } catch (IOException e) {//Catches any input/output errors
            System.out.println(" Error saving transaction: " + e.getMessage());//Displays error message to the user with technical details of what went wrong.
        }
    }

    // a static method that Reads all transactions from the CSV file
    public static List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();//Creates a new ArrayList to store all transactions
        File file = new File(FILE_NAME);//Creates a File object

        if (!file.exists()) {//Checks whether the file actually exists.
            System.out.println("No transactions file found — a new one will be created.");
            return transactions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {//Opens a BufferedReader to read from the file line by line
            String line;//Declares a variable to store each line read from the file
            while ((line = reader.readLine()) != null) {//Reads one line at a time until reaching the end of the file and returns null when done
                String[] parts = line.split("\\|");//Splits the line into separate pieces (fields) wherever there’s a pipe (|)
                if (parts.length == 5) {
                    Transaction t = new Transaction(parts[0], parts[1], parts[2], parts[3], Double.parseDouble(parts[4]));//Creates a new Transaction object while (parts[4]) is converted from a string to a number using Double.parseDouble().
                    transactions.add(t);//Adds the newly created Transaction object to my list.
                }
            }
        } catch (IOException e) {//Handles errors that might occur during file reading
            System.out.println("Error reading transactions: " + e.getMessage());
        }

        return transactions;//Returns the complete list of all objects loaded from the CSV file.
    }
}

