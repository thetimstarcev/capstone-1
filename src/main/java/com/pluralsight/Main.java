package com.pluralsight;

import java.util.Scanner;

public class Main {
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

        String userInput = scanner.nextLine();

        switch (userInput) {
            case "D":
                addDeposit();
                break;
            case "P":
                makePayment();
                break;
            case "L":
                ledger();
                break;
            case "X":
                break;
            default:
                System.out.println("Invalid input. Please try again.");

        }

    }

    private static void addDeposit() {
        System.out.println("--- Add Deposit---");
        System.out.println("Please enter the transaction description: ");
        String userInput = scanner.nextLine();
        System.out.println("Please enter vendor: ");
        userInput = scanner.nextLine();
        System.out.println("Please enter the amount:");
        userInput = scanner.nextLine();
    }

    private static void makePayment() {
        System.out.println("--- Make Payment---");
        System.out.println("Please enter the transaction description: ");
        String userInput = scanner.nextLine();
        System.out.println("Please enter vendor: ");
        userInput = scanner.nextLine();
        System.out.println("Please enter the amount:");
        userInput = scanner.nextLine();
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
