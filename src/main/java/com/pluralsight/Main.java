package com.pluralsight;// package declaration which is the name of the package.

//My import statements tell the program which external packages or libraries i want to use in my code.
import java.util.*;//Reading user input such as (Scanner) and Handling lists of transactions (objects)
import java.time.*;//Getting the current date and current time
import java.time.format.DateTimeFormatter;//Imports the class thus allows me to format time or date into a custom pattern


public class Main {//Declares my main class, named Main,the Main.java is The core control center that handles all user interactions, menus, and program logic.
    public static void main(String[] args) {//Defines the main method that will be called when my Java program starts.
        TransactionManager.initializeCSV();//method invocation for which TransactionManager is The class on which the method is being called and initializeCSV()The method being executed.
        Scanner scanner = new Scanner(System.in);  //Creates a new Scanner object for keyboard input
        showLedger(scanner);


        boolean running = true;//a boolean flag that controls the main loop so When running becomes false, the program exits the loop and stops

        System.out.println("========================================");
        System.out.println("    Welcome to Financial Tracker ");//Display of a header to make the app look polished when it starts.
        System.out.println("========================================");


        while (running) {//a loop that keeps showing the menu and waiting for user input until the user chooses to exit
            showHomeScreen(); // Displays of the available options
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
                    showLedger(scanner);  // View all transactions
                    break;
                case "X":
                    running = false; // an assignment statement that Ends the loop and exits
                    System.out.println("\nExiting application. Goodbye!");
                    break;// a break serves as a Exits block so it doesn’t “fall through” to the next case
                default:
                    System.out.println(" Invalid option. Please choose D, P, L, or X.");
            }//closing curly bracket.
        }//closing curly bracket.
    }
    // Displays the Screen menu method
    private static void showHomeScreen() {//A helper method that prints out the main menu
        System.out.println("\n=========== MAIN MENU ===========");
        System.out.println("D) Add Deposit");
        System.out.println("P) Make Payment (Debit)");
        System.out.println("L) Ledger");
        System.out.println("X) Exit");
        System.out.println("=================================");
    }
    // Add Deposit Method
    // Prompts the user for deposit details and saves them to the CSV file
    private static void addDeposit(Scanner scanner) {//private is my Access modifier meaning only code inside this class can call this method.
        System.out.println("\n--- Add Deposit ---");
        System.out.print("Enter description: ");//what the deposit is for
        String description = scanner.nextLine();
        System.out.print("Enter vendor/source: ");//where it came from
        String vendor = scanner.nextLine();//Declares a variable and captures what the user types and stores it as a string for later use
        System.out.print("Enter deposit amount: ");
        double amount = getValidAmount(scanner); //  a helper method for safe input ensuring that the user enters a valid number

//import statements
        LocalDate date = LocalDate.now();//gets today’s date
        LocalTime time = LocalTime.now();//gets current time

//Package Declaration for which external libraries or classes are to be made available in this file
        Transaction transaction = new Transaction(//Calls the Transaction constructor to create a new transaction
                date.toString(),//Converts date to a String
                time.format(DateTimeFormatter.ofPattern("HH:mm:ss")),//Formats time by giving the hours:minutes:seconds.
                description,//Passes all values (date, time, description, vendor, amount) into the object.
                vendor,
                amount
        );
//method invocation (A method call) a static method call.
        TransactionManager.saveTransaction(transaction);// saves input to csv file
        System.out.println("Deposit added successfully!");// outputs successful deposit message
    }
    // Make Payment (Debit) Method
    // Prompts the user for payment details and saves them as a negative to indicate money leaving your account.
    private static void makePayment(Scanner scanner) {// scanner,scanner is reading the input
        System.out.println("\n--- Make Payment (Debit) ---");
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Enter payment amount: ");
        double amount = getValidAmount(scanner);// a method call to get the getValidAmount

        //an if statement with a Boolean Expression and a Assignment statement thus If the condition is true, it negates the amount (turns a positive value into a negative one).
        if (amount > 0) amount = -amount;

//import statements
        LocalDate date = LocalDate.now();//Gets the current date and time
        LocalTime time = LocalTime.now();

        //a constructor caller for object creations . Creates a new Transaction object holds all the details for that one payment entry and  adds a new line in the CSV file.
        Transaction payment = new Transaction(
                date.toString(),
                time.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                description,
                vendor,
                amount
        );
        //a static method call that Saves data to the CSV file
        TransactionManager.saveTransaction(payment);
        System.out.println("Payment recorded successfully!");
    }
    // Ledger Method is private meaning Only this class can access it.
    // Displays all saved transactions from transactions.csv in a neat table
    private static void showLedger(Scanner scanner) {//a private static method The method reads each line from transactions.csv, splits it by pipes (|), and creates Transaction objects, the Scanner parameter allows reading user input for choices.
        boolean viewingLedger = true;//Declares a loop control variable As long as this is true, the ledger screen stays active

        while (viewingLedger) {//while loopd that Displays the Ledger menu options every time the loop runs.
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

            List<Transaction> transactions = TransactionManager.loadTransactions();// a variable declaration with initialization via a static method caller that Calls a method from another class
            Collections.reverse(transactions); // reverses the list order so the newest transactions show first.

//switch statement (or switch-case block)A control flow structure that lets you execute different code blocks depending on user input
            switch (choice) {
                case "A":
                    displayTransactions(transactions, "All Transactions");//Shows all transactions
                    break;// a break serves as a Exits block so it doesn’t “fall through” to the next case
                case "D":
                    displayTransactions(filterDeposits(transactions), "Deposits Only");//removes all negative amounts (leaving only deposits).
                    break;
                case "P":
                    displayTransactions(filterPayments(transactions), "Payments Only");//keeps only transactions with negative amounts (expenses).
                    break;
                case "R":
                    showReports(scanner, transactions);//Takes the user to the Reports Menu
                    break;
                case "0"://Ends the ledger loop and goes back to the previous menu.
                    viewingLedger = false;//a assignment statement that is a boolean variable that controls the loop
                    break;
                case "H":// goes back to Home screen
                    return;
                default:
                    System.out.println("Invalid option, please try again.");//Handles invalid input by prompting the user to re-enter a valid choice.
            }//closing curly bracket.
        }//closing curly bracket.
    }
    //displays a Reports menu method accepts the user’s choice, and runs report filters
    private static void showReports(Scanner scanner, List<Transaction> transactions) {
        boolean viewingReports = true;// boolean flag controls the while loop

        while (viewingReports) {//a while loop that repeatedly prints the menu header and options.
            System.out.println("\n=========== REPORTS MENU ===========");
            System.out.println("1) Month-to-Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year-to-Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");
            System.out.println("====================================");
            System.out.print("Select a report option: ");

            String choice = scanner.nextLine().trim();// a string line that Reads the user’s input (their menu choice) and trims whitespace

//switch statement (or switch-case block)A control flow structure that lets you execute different code blocks depending on user input
            switch (choice) {//a switch to branch based on the user’s selection.
                case "1":
                    LocalDate now = LocalDate.now();//a case block that gets today’s date (system clock).
                    List<Transaction> mtd = filterByMonth(transactions, now.getYear(), now.getMonthValue());//returns transactions whose date string
                    displayTransactions(mtd, "Month-to-Date");//matches the current year and month.
                    break;//leaves the switch and returns to the top of the reports loop
                case "2":
                    LocalDate prevMonth = LocalDate.now().minusMonths(1);//computes the date exactly one month earlier
                    List<Transaction> pm = filterByMonth(transactions, prevMonth.getYear(), prevMonth.getMonthValue());
                    displayTransactions(pm, "Previous Month");
                    break;
                case "3":
                    int currentYear = LocalDate.now().getYear();//Gets current year and uses filterByYear to include all transactions in that year.
                    List<Transaction> ytd = filterByYear(transactions, currentYear);
                    displayTransactions(ytd, "Year-to-Date");
                    break;
                case "4":
                    int prevYear = LocalDate.now().getYear() - 1;//Calculates last year (currentYear - 1) and displays those transactions.
                    List<Transaction> py = filterByYear(transactions, prevYear);
                    displayTransactions(py, "Previous Year");
                    break;
                case "5":
                    System.out.print("Enter vendor name to search: ");//Prompts the user for a vendor string, trims it.
                    String vendorName = scanner.nextLine().trim();
                    List<Transaction> vendorResults = filterByVendor(transactions, vendorName);
                    displayTransactions(vendorResults, "Transactions for Vendor: " + vendorName);
                    break;
                case "6"://Calls runCustomSearch(scanner) (defined below) to handle a more complex interactive filter.
                    runCustomSearch(scanner);
                    break;
                case "0":
                    viewingReports = false;//Sets the loop flag to false; after break the while condition fails and the method returns to the caller.
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");//Handles invalid input.
            }// closing curly bracket.ends switch
        }//closing curly bracket. ends while
    }
    //The runCustomSearch method interacts with the user to build a set of filters (start/end date, description substring, vendor substring, exact amount)
    //and then runs those filters against the transactions list, printing matches.
    private static void runCustomSearch(Scanner scanner) {
        System.out.println("\n=========== CUSTOM SEARCH ===========");//Prints header
        System.out.println("Leave any field blank to skip filtering by it.");

        // Prompt the user for all possible filters
        System.out.print("Start Date (YYYY-MM-DD): ");
        String startInput = scanner.nextLine().trim();
                                                 //date strings in YYYY-MM-DD (ISO) format.
        System.out.print("End Date (YYYY-MM-DD): ");
        String endInput = scanner.nextLine().trim();

        System.out.print("Description Contains: ");
        String descInput = scanner.nextLine().trim().toLowerCase();//text filters; converted to lower case for case-insensitive matching.

        System.out.print("Vendor Contains: ");
        String vendorInput = scanner.nextLine().trim().toLowerCase();//text filters; converted to lower case for case-insensitive matching.

        System.out.print("Amount (Exact Match): ");
        String amountInput = scanner.nextLine().trim();

        // Load transactions from the CSV
        List<Transaction> transactions = TransactionManager.loadTransactions();//a Variable declaration and initialization The variable transactions is a List of Transaction objects.
        if (transactions.isEmpty()) {//an if statement (conditional statement).
            System.out.println("No transactions found.");//A method call that prints a message to the console.
            return;//a return statement that If none found, prints message and returns early.
        }

        // Variable declarations that Convert date strings safely and Initializes the filter variables as null (meaning “no filter”).
        LocalDate startDate = null;
        LocalDate endDate = null;
        Double amountFilter = null;

//a Exception handling block full of if statements that is used for Error handling and Input validation -(try catch block)-
        try {//ensures that if a user enters an invalid date or amount, the program doesn’t crash.
            if (!startInput.isEmpty()) startDate = LocalDate.parse(startInput);//Tries to parse each input if it isn’t blank:
            if (!endInput.isEmpty()) endDate = LocalDate.parse(endInput);//parses an ISO YYYY-MM-DD string into a LocalDate
            if (!amountInput.isEmpty()) amountFilter = Double.parseDouble(amountInput);//converts the numeric string into a Double.
        } catch (Exception e) {//If parsing fails
            System.out.println(" Invalid input format. Please check your dates or amount.");//Prints an error and returns (no search performed)
            return;//a return statement
        }

        // Filter results
        List<Transaction> results = new ArrayList<>();//Declares and initializes an empty list to store transactions that match the filters.

// A for-each loop that iterates over every Transaction object in the transactions list.
        for (Transaction t : transactions) {//Loops over each transaction
            LocalDate tDate = LocalDate.parse(t.getDate());//Parses transaction date strings into a LocalDates
            boolean matches = true;//flag that starts as true. Later in your code,
// you check various conditions (like date ranges, descriptions, vendor names, amounts). If any condition fails, you set matches = false.  -(match method )-

            // if statments for date range filters:
            if (startDate != null && tDate.isBefore(startDate)) matches = false;// if filter exists and the transaction date is before it

            if (endDate != null && tDate.isAfter(endDate)) matches = false;//filter exists and the transaction date is after it

        //if statements for Text filters
            if (!descInput.isEmpty() && !t.getDescription().toLowerCase().contains(descInput)) matches = false;
                                                                                                      //If user provided description Input, the code lowercases the
            if (!vendorInput.isEmpty() && !t.getVendor().toLowerCase().contains(vendorInput)) matches = false;//transaction description and checks substring This implements case-insensitive substring search.

            if (amountFilter != null && t.getAmount() != amountFilter) matches = false;//If the user entered an amount, only transactions with exactly that amount (double equality) pass.

            if (matches) results.add(t);//If the transaction passed all tested filters, it’s added to results.
        }

        //a if-else statement combined with a for-each loop that Display results
        if (results.isEmpty()) {//a conditional statement with a for-each loop
            System.out.println("\nNo transactions matched your search criteria.");//If no matches, informs the user.
        } else {//an else block for If there are matches:
            Collections.reverse(results); // Reverses the results so newest transactions display first.
            System.out.println("\n--- Custom Search Results ---");
            printLedgerHeader();//Calls a method to Print a header
            for (Transaction t : results) {//A for-each loop that iterates over the filtered results and prints each transaction.
                printLedgerEntry(t);//Iterates through results and prints each transaction row
            }
        }
    }


    // FILTER METHODS
    private static List<Transaction> filterDeposits(List<Transaction> transactions) {//Creates a new empty list named deposits
        List<Transaction> deposits = new ArrayList<>();//Loops through every transaction.
        for (Transaction t : transactions) {
            if (t.getAmount() > 0) deposits.add(t);//If the transaction’s amount is positive, it’s considered a deposit and added to the list.
        }
        return deposits;//a Returns statment that list deposits.
    }
    private static List<Transaction> filterPayments(List<Transaction> transactions) {//Same as above, but filters negative amounts (expenses or outgoing payments).
        List<Transaction> payments = new ArrayList<>();//creates a new empty list
        for (Transaction t : transactions) {//a for-each loop that goes through every Transaction in the transactions list.
            if (t.getAmount() < 0) payments.add(t);//if statement that checks if the transaction’s amount
        }
        return payments;// return statement
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

    // DISPLAY METHODS
    private static void displayTransactions(List<Transaction> transactions, String title) {
        System.out.println("\n--- " + title + " ---");//Prints the title
        if (transactions.isEmpty()) {//An if statement to check if the list is empty.
            System.out.println("No transactions found.");//Checks if the list is empty and stops early if there’s nothing to show.
            return;
        }

        //System.out.printf for formatted output. Table-style output Prints a formatted table header using printf()
        System.out.printf("%-12s %-10s %-30s %-20s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("--------------------------------------------------------------------------------------");
//12-character column for Date
//10-character column for Time
//30-character column for Description
//20-character column for Vendor
//10-character numeric column for Amount.

        for (Transaction t : transactions) {//A for-each loop to iterate over transactions.Loops through each transaction and prints it in the same formatted table
            System.out.printf("%-12s %-10s %-30s %-20s %-10.2f%n",
                    t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
        }
    }
    //a Method that Ensures the user enters a valid numeric value.
    private static double getValidAmount(Scanner scanner) {
        while (true) {//Reads input.
            try {
                String input = scanner.nextLine().trim();//Trims spaces.
                return Double.parseDouble(input);//Attempts to parse it to a double.
            } catch (NumberFormatException e) {//A try-catch block for exception handling that returns that number if successful
                System.out.print("Invalid number. Please enter a valid amount: ");//catches the exception and re-prompts the user if un successful
            }
        }
    }// a Method that Prints the table header for ledger display
            private static void printLedgerHeader() {
                System.out.printf("%-12s %-10s %-30s %-20s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");//a System.out.printf for formatted header.
                System.out.println("--------------------------------------------------------------------------------------");//a System.out.println for a dividing line.
            }
            // a Method that Prints one transaction row in formatted form
            private static void printLedgerEntry(Transaction t) {
                System.out.printf("%-12s %-10s %-30s %-20s %-10.2f%n",// a System.out.printf to format the transaction fields.
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());// a multiple method call that's calling a getter method
            }                                                                            //of the Transaction class to retrieve a specific piece of data from that object.
        }// closing curly brackets






