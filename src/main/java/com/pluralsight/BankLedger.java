package com.pluralsight;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class BankLedger {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        mainMenu();
        System.out.println("Have a great name!");
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
        System.out.println(prompt);

        String userInput = scanner.nextLine();

        switch (userInput) {
            case "D","d":
                addDeposit();
                break;
            case "P","p":
                makePayment();
                break;
            case "L","l":
                ledger();
                break;
            case "X","x":
                break;
            default:
                System.out.println("Invalid input. Please try again.");

        }

    }

    private static void addDeposit() {
        System.out.println("--- Add Deposit---");
        System.out.println("Please enter the transaction description: ");
        String description = scanner.nextLine();

        System.out.println("Please enter vendor: ");
        String vendor = scanner.nextLine();

        System.out.println("Please enter the amount:");
        double amount = scanner.nextDouble();

        LocalDate date = LocalDate.now(); // Adding date anf formatting it
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalTime time = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");


        //Adding the information and save it to the csv file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv",true));
            writer.write(date.format(dateFormatter) + " | " + time.format(timeFormatter) + " | " + description + " | " + vendor + " | " + amount);
            writer.newLine();
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
        double amount = scanner.nextDouble();




        System.out.println("Payment added:");
        System.out.println(description + " | " + vendor + " | " + amount);

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
        String userInput = scanner.nextLine();
        switch (userInput) {
            case "A":
        }
    }
}
