package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class BankLedger {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transaction> transactionList = new ArrayList<>();
    static void loadTransactions(){
        BufferedReader reader = null;
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
    }

    public static String getCurrentDateTime () {
        LocalDate date = LocalDate.now(); // Adding date anf formatting it
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalTime time = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return (date.format(dateFormatter) + "|" + time.format(timeFormatter) + "|");
    }

    public static void main(String[] args) {
        mainMenu();
        System.out.println("Have a great day!");
    }

    private static void mainMenu() {
        String prompt = """
                Welcome to Your Bank Ledger
                
                D) Add Deposit
                P) Make Payment
                L) Ledger
                X) Exit
                
                Choose an option:
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
                    ledger();
                    break;
                case "X", "x":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        } while (running);
    }

    private static void addDeposit() {
        System.out.println("--- Add Deposit---");
        System.out.println("Please enter the transaction description: ");
        String description = scanner.nextLine();

        System.out.println("Please enter vendor: ");
        String vendor = scanner.nextLine();

        System.out.println("Please enter the amount:");
        double amount = scanner.nextDouble();

        //Adding the information and save it to the csv file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv",true));
            writer.newLine();
            writer.write(getCurrentDateTime() + description + "|" + vendor + "|" + amount);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving your transaction");
        }
        System.out.println("Deposit added!");
    }

    private static void makePayment() {
        System.out.println("--- Make Payment---");
        System.out.println("Please enter the transaction description: ");
        String description = scanner.nextLine();

        System.out.println("Please enter vendor: ");
        String vendor = scanner.nextLine();

        System.out.println("Please enter the amount:");
        double amount = scanner.nextDouble() *-1; //make the amount negative


        //Adding the information and save it to the csv file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv", true));
            writer.newLine();
            writer.write(getCurrentDateTime() + description + "|" + vendor + "|" + amount);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving your transaction.");;
        }
        System.out.println("Payment added!");
    }

    private static void ledger() {
        String prompt = """
                A) All
                D) Deposits
                P) Payments
                R) Reports
                H) Home
                
                Choose an option:
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

    private static void displayAll() {
        loadTransactions();
        for (Transaction transaction : transactionList) {
            System.out.println(transaction.getDate() + " | " + transaction.getTime() + " | " + transaction.getDescription() + " | " + transaction.getVendor() + " | " + transaction.getAmount());
            }
    }

    private static void displayDeposits() {
        loadTransactions();
        for (Transaction transaction : transactionList) {
            if (transaction.getAmount() > 0) {
                System.out.println(transaction.getDate() + " | " + transaction.getTime() + " | " + transaction.getDescription() + " | " + transaction.getVendor() + " | " + transaction.getAmount());
            }
        }
    }
    private static void displayPayments() {
        loadTransactions();
        for (Transaction transaction : transactionList) {
            if (transaction.getAmount() < 0) {
                System.out.println(transaction.getDate() + " | " + transaction.getTime() + " | " + transaction.getDescription() + " | " + transaction.getVendor() + " | " + transaction.getAmount());
            }
        }
    }
    private static void displayReports() {
    }






}
