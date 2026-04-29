package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class BankLedger {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transaction> transactionList = new ArrayList<>();
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static void main(String[] args) {
        mainMenu();
        System.out.println("Thank you for using our App!");
    }

    private static void loadTransactions() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));
            reader.readLine(); //skip header

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0]);
                LocalTime time = LocalTime.parse(parts[1]);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                transactionList.add(transaction);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error displaying your transactions.");
        }
        sortTransaction();
    }

    public static void sortTransaction(){
        transactionList.sort(Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime).reversed());
    }

    private static LocalDate getDate() {
        LocalDate date = null;
        boolean validDate = false;
        do {
            try {
                System.out.println("Please enter the transaction date (MM/DD/YYYY): ");
                String dateInput = scanner.nextLine(); //enter US date format
                date = LocalDate.parse(dateInput, DATE_FORMATTER);

                validDate = true;
            } catch (Exception e) {
                System.out.println("Invalid time format. Try again.");
            }
        } while (!validDate);
        return date;
    }

    private static LocalTime getTime() {
        LocalTime time = null;
        boolean validTime = false;
        do {
            System.out.println("Please enter the transaction time (HH:mm or HH:mm:ss): ");
            String timeInput = scanner.nextLine();

            // adding 0 and seconds if hour is entered as a single number
            if (timeInput.length() == 4 && timeInput.contains(":")) {
                timeInput = "0" + timeInput + ":00";
            }
            // adding 0 if hour entered as a single number
            else if (timeInput.length() == 7 && timeInput.contains(":")) {
                timeInput = "0" + timeInput;
            }

            // adding seconds if not entered
            else if (timeInput.length() == 5) {
                timeInput = timeInput + ":00";
            }
            try {
                time = LocalTime.parse(timeInput);
                validTime = true;
            } catch (Exception e) {
                System.out.println("Invalid time format. Try again.");
            }
        } while (!validTime);
        return time;
    }

    //Displaying Main Menu
    private static void mainMenu() {
        String prompt = """
                Welcome to Your Bank Ledger
                
                D) Add Deposit
                P) Make Payment
                L) Ledger
                X) Exit
                
                Please choose an option:
                """;
        boolean running = true;
        do {
            System.out.println(prompt);
            String userInput = scanner.nextLine();

            switch (userInput) {
                case "D", "d":
                    addDeposit();
                    break;
                case "P", "p":
                    makePayment();
                    break;
                case "L", "l":
                    displayLedger();
                    break;
                case "X", "x":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        } while (running);
    }

    //Adding Deposit
    private static void addDeposit() {
        System.out.println("--- Add Deposit---");
        LocalDate date = null;
        LocalTime time = null;

        boolean done = false;
        do {
            System.out.println("Would you like to use current date and time? (yes/no):");
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("Yes")) {
                date = LocalDate.now();
                time = LocalTime.now();
                done = true;
            } else if (userInput.equalsIgnoreCase("No")) {
                date = getDate();
                time = getTime();
                done = true;
            } else {
                System.out.println("Invalid input. Please try again");
            }
        } while (!done);

        System.out.println("Please enter the transaction description: ");
        String description = scanner.nextLine();

        System.out.println("Please enter vendor: ");
        String vendor = scanner.nextLine();

        System.out.println("Please enter the amount:");
        double amount = scanner.nextDouble();


        //Adding the information and save it to the csv file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv", true));
            writer.newLine();
            writer.write(date + "|" + time.format(TIME_FORMATTER) + "|" + description + "|" + vendor + "|" + amount);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving your transaction");
        }
        System.out.println("Deposit added!");
        System.out.println("Would you like to add another deposit? (yes/no): ");

    }

    // Making Payment
    private static void makePayment() {
        System.out.println("--- Make Payment---");
        LocalDate date = null;
        LocalTime time = null;

        boolean done = false;
        do {
            System.out.println("Would you like to use current date and time? (yes/no):");
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("Yes")) {
                date = LocalDate.now();
                time = LocalTime.now();
                done = true;
            } else if (userInput.equalsIgnoreCase("No")) {
                date = getDate();
                time = getTime();
                done = true;
            } else {
                System.out.println("Invalid input. Please try again");
            }
        } while (!done);

        System.out.println("Please enter the transaction description: ");
        String description = scanner.nextLine();

        System.out.println("Please enter vendor: ");
        String vendor = scanner.nextLine();

        System.out.println("Please enter the amount:");
        double amount = scanner.nextDouble() * -1; //make the amount negative


        //Adding the information and save it to the csv file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv", true));
            writer.newLine();
            writer.write(date + "|" + time.format(TIME_FORMATTER) + "|" + description + "|" + vendor + "|" + amount);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving your transaction.");
        }
        System.out.println("Payment added!");
    }

    //Displaying Ledger submenu
    private static void displayLedger() {
        String prompt = """
                --- Ledger---
                
                A) All
                D) Deposits
                P) Payments
                R) Reports
                H) Home
                
                Please choose an option:
                """;
        boolean running = true;

        do {
            System.out.println(prompt);
            String userInput = scanner.nextLine();

            switch (userInput) {
                case "A", "a":
                    displayAll();
                    break;
                case "D", "d":
                    displayDeposits();
                    break;
                case "P", "p":
                    displayPayments();
                    break;
                case "R", "r":
                    displayReports();
                    break;
                case "H", "h":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        } while (running);
    }

    /**
     * Display all transactions on the screen
     */
    private static void displayAll() {
        loadTransactions();
        for (Transaction transaction : transactionList) {
            transaction.displayTransactions();
        }
    }

    private static void displayDeposits() {
        loadTransactions();
        for (Transaction transaction : transactionList) {
            if (transaction.getAmount() > 0) {
                transaction.displayTransactions();
            }
        }
    }

    private static void displayPayments() {
        loadTransactions();
        for (Transaction transaction : transactionList) {
            if (transaction.getAmount() < 0) {
                transaction.displayTransactions();
            }
        }
    }

    private static void displayReports() {
        String prompt = """
                --- Reports---
                
                1) Month To Date
                2) Previous Month
                3) Year To Date
                4) Previous Year
                5) Search by Vendor
                6) Custom Search
                0) Back
                
                Please choose an option:
                """;
        boolean running = true;

        do {
            System.out.println(prompt);
            String userInput = scanner.nextLine();

            switch (userInput) {
                case "1":
                    displayMonthToDate();
                    break;
                case "2":
                    displayPreviousMonth();
                    break;
                case "3":
                    displayYearToDate();
                    break;
                case "4":
                    displayPreviousYear();
                    break;
                case "5":
                    searchByVendor();
                    break;
                case "6":
                    customSearch();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        } while (running);
    }

    private static void displayMonthToDate() {
        loadTransactions();
        LocalDate today = LocalDate.now();
        for (Transaction transaction : transactionList) {
            if (transaction.getDate().getYear() == today.getYear() && transaction.getDate().getMonthValue() == today.getMonthValue()) {
                transaction.displayTransactions();
            }
        }
    }

    private static void displayPreviousMonth() {
        loadTransactions();
        LocalDate today = LocalDate.now();
        for (Transaction transaction : transactionList) {
            if (transaction.getDate().getYear() == today.getYear() && transaction.getDate().getMonthValue()==today.getMonthValue()-1) {
                transaction.displayTransactions();
            }
        }
    }

    private static void displayYearToDate() {
        loadTransactions();
        LocalDate today = LocalDate.now();
        for (Transaction transaction : transactionList) {
            if (transaction.getDate().getYear() == today.getYear()) {
                transaction.displayTransactions();
            }
        }
    }

    private static void displayPreviousYear() {
        loadTransactions();
        int previousYear = LocalDate.now().getYear() -1;
        for (Transaction transaction : transactionList) {
            if (transaction.getDate().getYear() == previousYear) {
                transaction.displayTransactions();
            }
        }
    }

    private static void searchByVendor () {
        System.out.println("--- Search Your Transaction by Vendor---");
        System.out.println("Please enter the name of the Vendor: ");
        String vendor = scanner.nextLine();
        loadTransactions();
        for (Transaction transaction : transactionList) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                transaction.displayTransactions();
            }
        }
    }

    private static void customSearch() {
        loadTransactions();
        System.out.println("--- Custom Search---");
        System.out.println("Please enter start date (MM/DD/YYYY) or press Enter to skip: ");
        String startDateInput = scanner.nextLine();

        System.out.println("Please enter end date (MM/DD/YYYY) or press Enter to skip: ");
        String endDateInput = scanner.nextLine();

        System.out.println("Please enter transaction description or press Enter to skip: ");
        String descriptionInput = scanner.nextLine();

        System.out.println("Please enter transaction vendor or press Enter to skip: ");
        String vendorInput = scanner.nextLine();

        System.out.println("Please enter the amount or press Enter to skip: ");
        String amountInput = scanner.nextLine();

        ArrayList<Transaction> result = transactionList;
        result = filterByStartDate(startDateInput, result);
        result = filterByEndDate(endDateInput, result);
        result = filterByDescription(descriptionInput, result);
        result = filterByVendor(vendorInput, result);
        result = filterByAmount(amountInput, result);

        System.out.println("---Your custom report:---");
        //displayTransactions(result);
        }
//
//        displayAll();
//        displayTransactions(transactionList);



    private static ArrayList<Transaction> filterByStartDate(String startDateInput, ArrayList<Transaction> result) {
        if (startDateInput.isBlank()) {
            return result;
        }
        ArrayList<Transaction> filtered = new ArrayList<>();
        LocalDate startDate = LocalDate.parse(startDateInput, DATE_FORMATTER);
        for (Transaction transaction : result) {
            if (transaction.getDate().isAfter(startDate)) filtered.add(transaction);
        }
        return filtered;
    }

    private static ArrayList<Transaction> filterByEndDate(String endDateInput, ArrayList<Transaction> result) {
        if (endDateInput.isBlank()) {
            return result;
        }
        ArrayList<Transaction> filtered = new ArrayList<>();
        LocalDate startDate = LocalDate.parse(endDateInput, DATE_FORMATTER);
        for (Transaction transaction : result) {
            if (transaction.getDate().isBefore(startDate)) filtered.add(transaction);
        }
        return filtered;
    }

    private static ArrayList<Transaction> filterByDescription(String descriptionInput, ArrayList<Transaction> result) {
        if (descriptionInput.isBlank()) {
            return result;
        }
        ArrayList<Transaction> filtered = new ArrayList<>();
        for (Transaction transaction : result) {
            if (transaction.getDescription().equalsIgnoreCase(descriptionInput)) filtered.add(transaction);
        } return filtered;
    }

    private static ArrayList<Transaction> filterByVendor(String vendorInput, ArrayList<Transaction> result) {
        if (vendorInput.isBlank()) {
            return result;
        }
        ArrayList<Transaction> filtered = new ArrayList<>();
        for (Transaction transaction : result) {
            if (transaction.getVendor().equalsIgnoreCase(vendorInput)) filtered.add(transaction);
            }
            return filtered;
        }

    private static ArrayList<Transaction> filterByAmount(String amountInput, ArrayList<Transaction> result) {
        if (amountInput.isBlank()) {
            return result;
        }
        double amount = Double.parseDouble(amountInput);
        ArrayList<Transaction> filtered = new ArrayList<>();
        for (Transaction transaction : result) {
            if (transaction.getAmount() == amount) filtered.add(transaction);
        } return filtered;
    }
}


