package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class BudgetMap {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transaction> transactionList = new ArrayList<>();
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * Prints the table header for the transaction list.
     */
    public static void printHeader () {
        System.out.printf("%-12s %-12s %-30s %-25s %-15s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("----------------------------------------------------------------------------------------------");
    }

    public static void main(String[] args) {
        mainMenu();
        System.out.println("Thank you for using our App!");
    }

    /**
     * Loads transactions from the CSV file into the transaction list.
     */
    private static void loadTransactions() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));
            reader.readLine(); // Skip the header row in the CSV file

            String line;
            // Read each line from the file until the end
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                // Convert string values into correct data types
                LocalDate date = LocalDate.parse(parts[0]);
                LocalTime time = LocalTime.parse(parts[1]);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);


                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                transactionList.add(transaction); // Add transaction to the main list
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error displaying your transactions.");
        }
        sortTransaction();
    }

    /**
     * Sorts transactions by date and time, newest first.
     */
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

            // Adding 0 and seconds if hour is entered as a single number
            if (timeInput.length() == 4 && timeInput.contains(":")) {
                timeInput = "0" + timeInput + ":00";
            }
            // Adding 0 if hour entered as a single number
            else if (timeInput.length() == 7 && timeInput.contains(":")) {
                timeInput = "0" + timeInput;
            }

            // Adding seconds if not entered
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

    /**
     * Displays the main menu and handles the user's main choice.
     */
    private static void mainMenu() {
        String prompt = """
                
                        ╭╮ ╷ ╷╶┬╮╭─╴╭─╴╶┬╴╭┬╮╭─╮╭─╮
                        ├┴╮│ │ │││╶╮├╴  │ │││├─┤├─╯
                        ╰─╯╰─╯╶┴╯╰─╯╰─╴ ╵ ╵ ╵╵ ╵╵ \s
                ==========================================
                             💰 BudgetMap 💰
                         Personal Budget Tracker
                ==========================================
                Track your income, expenses, and spending
                
                💰 Add Income                          (D)
                📉 Record Expense                      (P)
                📊 View Budget Ledger                  (L)
                ❌ Exit                                (X)
                __________________________________________
                
                Please choose an option:
                """
                ;
        boolean running = true;
        do {
            System.out.println(prompt);
            String userInput = scanner.nextLine();

            switch (userInput) {
                case "D", "d":
                    addIncome();
                    break;
                case "P", "p":
                    addExpense();
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

    /**
     * Adds an income transaction and saves it to the CSV file.
     */
    private static void addIncome() {
        System.out.println();
        System.out.println("=============== ADD INCOME ===============");
        LocalDate date = null;
        LocalTime time = null;

        boolean done = false;
        do {
            System.out.println("Would you like to use current date and time? (yes/no):");
            String userInput = scanner.nextLine();
            // If user wants to use current date and time
            if (userInput.equalsIgnoreCase("Yes")) {
                date = LocalDate.now();
                time = LocalTime.now();
                done = true;
            }
            // If user wants to enter custom date and time
            else if (userInput.equalsIgnoreCase("No")) {
                date = getDate();
                time = getTime();
                done = true;
            } else {
                System.out.println("Invalid input. Please try again");
            }
        } while (!done);

        System.out.println("Please enter income description: ");
        String description = scanner.nextLine();

        System.out.println("Please enter vendor: ");
        String vendor = scanner.nextLine();

        System.out.println("Please enter amount:");
        String inputAmount = scanner.nextLine();
        double amount = getDoubleFromUser(inputAmount);


        //Adding the information and save it to the csv file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv", true));
            writer.newLine();
            writer.write(String.format(date + "|" + time.format(TIME_FORMATTER) + "|" + description + "|" + vendor + "|" + "%.2f", amount));
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving your income");
        }
        System.out.println("Income added successfully!");

        do {
            System.out.println("\nWould you like to add another income? (yes/no): ");
            String userAnswer = scanner.nextLine();
            if (userAnswer.equalsIgnoreCase("yes")) {
                addIncome();
                break;
            } else if (userAnswer.equalsIgnoreCase("no")) {
                break;
            } else System.out.println("Invalid input. Please try again!");
        } while (true);
    }

    /**
     * Display all transactions on the screen
     */
    private static void addExpense() {
        System.out.println();
        System.out.println("============== ADD EXPENSE ===============");
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

        System.out.println("Please enter expense description: ");
        String description = scanner.nextLine();

        System.out.println("Please enter vendor: ");
        String vendor = scanner.nextLine();

        System.out.println("Please enter the amount: ");
        String userInput = scanner.nextLine();
        double amount = getDoubleFromUser(userInput) * -1; // Store expenses as negative numbers

        //Adding the information and save it to the csv file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv", true));
            writer.newLine();
            writer.write(date + "|" + time.format(TIME_FORMATTER) + "|" + description + "|" + vendor + "|" + amount);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving your expense.");
        }
        System.out.println("Expense added successfully!");
        do {
            System.out.println("\nWould you like to add another expense? (yes/no): ");
            String userAnswer = scanner.nextLine();
            if (userAnswer.equalsIgnoreCase("yes")) {
                addExpense();
                break;
            } else if (userAnswer.equalsIgnoreCase("no")) {
                break;
            } else System.out.println("Invalid input. Please try again!");
        } while (true);
    }

    /**
     * Displays the ledger menu and lets the user choose which transactions to view.
     */
    private static void displayLedger() {
        String prompt = """
                
                ============= BUDGET LEDGER =============
                
                📂 All                                (A)
                💰 Income Only                        (D)
                💸 Expenses Only                      (P)
                📊 Budget Reports                     (R)
                🏠 Home                               (H)
                _________________________________________
                
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
                    displayIncome();
                    break;
                case "P", "p":
                    displayExpenses();
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
     * Displays all transactions.
     */
    private static void displayAll() {
        printHeader();
        loadTransactions();
        for (Transaction transaction : transactionList) {
            transaction.displayTransactions();
        }
    }

    /**
     * Displays only income transactions.
     */
    private static void displayIncome() {
        printHeader();
        loadTransactions();
        for (Transaction transaction : transactionList) {
            if (transaction.getAmount() > 0) {
                transaction.displayTransactions();
            }
        }
    }

    /**
     * Displays only expense transactions.
     */
    private static void displayExpenses() {
        printHeader();
        loadTransactions();
        for (Transaction transaction : transactionList) {
            if (transaction.getAmount() < 0) {
                transaction.displayTransactions();
            }
        }
    }

    /**
     * Displays the reports menu and handles report choices.
     */
    private static void displayReports() {
        String prompt = """
                
                ============ BUDGET REPORTS =============
                
                📅 Month To Date Spending             (1)
                ⏮️ Previous Month                     (2)
                📆 Year To Date                       (3)
                🗓️ Previous Year                      (4)
                🏪 Search by Vendor                   (5)
                🔍 Custom Search                      (6)
                🔙 Back                               (0)
                _________________________________________
                
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

    /**
     * Displays transactions from the current month.
     */
    private static void displayMonthToDate() {
        printHeader();
        loadTransactions();
        LocalDate today = LocalDate.now();
        for (Transaction transaction : transactionList) {
            if (transaction.getDate().getYear() == today.getYear() && transaction.getDate().getMonthValue() == today.getMonthValue()) {
                transaction.displayTransactions();
            }
        }
    }

    /**
     * Displays transactions from the previous month.
     */
    private static void displayPreviousMonth() {
        printHeader();
        loadTransactions();
        LocalDate today = LocalDate.now();
        for (Transaction transaction : transactionList) {
            if (transaction.getDate().getYear() == today.getYear() && transaction.getDate().getMonthValue()==today.getMonthValue()-1) {
                transaction.displayTransactions();
            }
        }
    }

    /**
     * Gets a valid double from user input.
     * @param prompt String
     * @return double value
     */
    public static double getDoubleFromUser(String prompt) {
        double amount;
        // Keep asking until user enters a valid number
        do {
            try {
                amount = Double.parseDouble(prompt);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid format. Please try again!");
                System.out.println("Please enter amount: ");
                prompt = scanner.nextLine();
            }
        } while (true);
        return amount;
    }

    /**
     * Displays transactions from the current year.
     */
    private static void displayYearToDate() {
        printHeader();
        loadTransactions();
        LocalDate today = LocalDate.now();
        for (Transaction transaction : transactionList) {
            if (transaction.getDate().getYear() == today.getYear()) {
                transaction.displayTransactions();
            }
        }
    }

    /**
     * Displays transactions from the previous year.
     */
    private static void displayPreviousYear() {
        printHeader();
        loadTransactions();
        int previousYear = LocalDate.now().getYear() -1;
        for (Transaction transaction : transactionList) {
            if (transaction.getDate().getYear() == previousYear) {
                transaction.displayTransactions();
            }
        }
    }

    /**
     * Searches and displays transactions that match the vendor name.
     */
    private static void searchByVendor () {
        System.out.println();
        System.out.println("=== Search Your Transaction by Vendor ===");
        System.out.println("Please enter the name of the Vendor: ");
        String vendor = scanner.nextLine();
        printHeader();
        loadTransactions();
        for (Transaction transaction : transactionList) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                transaction.displayTransactions();
            }
        }
    }

    /**
     * Lets the user search transactions using multiple optional filters.
     */
    private static void customSearch() {
        loadTransactions();
        System.out.println("============= CUSTOM SEARCH ==============");
        System.out.println("Please enter start date (MM/DD/YYYY) or press Enter to skip: ");
        String startDateInput = scanner.nextLine();

        System.out.println("Please enter end date (MM/DD/YYYY) or press Enter to skip: ");
        String endDateInput = scanner.nextLine();

        System.out.println("Please enter income/expense description or press Enter to skip: ");
        String descriptionInput = scanner.nextLine();

        System.out.println("Please enter vendor or press Enter to skip: ");
        String vendorInput = scanner.nextLine();

        System.out.println("Please enter the amount or press Enter to skip: ");
        String amountInput = scanner.nextLine();

        // Start with full list and narrow it down with each filter
        ArrayList<Transaction> result = transactionList;
        result = filterByStartDate(startDateInput, result);
        result = filterByEndDate(endDateInput, result);
        result = filterByDescription(descriptionInput, result);
        result = filterByVendor(vendorInput, result);
        result = filterByAmount(amountInput, result);

        System.out.println("============= CUSTOM REPORT ==============");
        displayTransactions(result); // Display filtered results
    }

    public static void displayTransactions(ArrayList<Transaction> transactions) {
        printHeader();
        for (Transaction transaction : transactions) {
            transaction.displayTransactions();
        }
    }

    /**
     * Filters transactions that occur after the given start date.
     * If no start date is provided, returns the original list.
     *
     * @param startDateInput the start date entered by the user
     * @param result the current transaction list
     * @return transactions after the start date
     */
    private static ArrayList<Transaction> filterByStartDate(String startDateInput, ArrayList<Transaction> result) {
        if (startDateInput.isBlank()) {
            return result; // If the user skips this filter, return the current list unchanged
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


