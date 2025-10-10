package com.pluralsight;

public class Transaction { // DECLARING MY PUBLIC CLASS ( public class meaning can be accessed from anywhere in my project.
    public static void main(String[] args) {

    }// private Means these variables cannot be accessed directly from outside the class
    private String date;//The date of the transaction
    private String time;//The time the transaction happened
    private String description;//details about what the transaction was
    private String vendor;//The name of the vendor
    private double amount;//The name of the vendor

//This is the constructor that is called when you create a new transaction object.
    // These constructors run and assign the provided arguments to the instance variables.
    public Transaction(String date, String time, String description, String vendor, double amount) {
        this.date = date; //current object
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public String toCSV() { // a method i chose to convert the transaction’s data into a CSV (Comma-Separated Values) format
        return String.format("%s|%s|%s|%s|%.2f", date, time, description, vendor, amount);
    }//pipes are used (|) as a separator

    @Override // @Override is used tells the compiler that the next method overrides a built-in method from the Object class to customize how your object is displayed when printed.
    public String toString() { // Defines how an object should appear when converted to text
        return String.format("%s %s - %s (%s): $%.2f", date, time, description, vendor, amount);
    }
}


