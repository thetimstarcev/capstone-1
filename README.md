# 💰 BudgetMap Personal Budget Tracker

## 📌 Project Overview
A simple command-line application built in Java to keep track of personal finances.  
The app is designed for basic financial tracking, allowing users to record income and expenses, view transactions, and better understand their spending habits.
<hr style="height:3px;">

## ✨ App Features
- Record income and expenses
- Store transactions in a CSV file
- View transaction history
- Generate basic financial reports
- Custom search with multiple filters
<hr style="height:3px;">

## ⚙️ Setup & Installation
1. Clone the repository:
```bash
git clone https://github.com/thetimstarcev/capstone-1.git
```
2. Open the project in your IDE (IntelliJ recommended)
3. Make sure you have Java installed (JDK 17+)
4. Run the `Main.java` file
<hr style="height:3px;">

## 📁 Project Structure
```plaintext
src/
├── main/java/com/pluralsight/
│   ├── BudgetMap.java
│   └── Transaction.java
├── main/resources/
│   └── transactions.csv
└── test/
```
### Structure Explanation
- `BudgetMap.java` → main logic of the app (menus, user interaction, and overall flow)
- `Transaction.java` → defines the Transaction object (date, time, amount, description, etc.)
- `transactions.csv` → stores all transactions (acts like a simple database)
- `test/` → reserved for future tests
<hr style="height:3px;">

## 🧭 User Flow
### From the home screen, the user can:
- Add income
- Record an expense
- Open the budget ledger
- Exit the app    

### The ledger allows the user to:
- View all transactions
- Switch to income-only view
- Switch to expenses-only view
- Open reports

### In Reports, the user can view transactions by:
- Month to date
- Previous month
- Year to date
- Previous year
- Search transactions by vendor
- Open custom search

### Apply one or multiple filters:
- Date range (start / end)
- Description
- Vendor
- Amount

### User can return to previous menus or exit at any time
<hr style="height:3px;">

## 💡 Code Highlight
One part I’m particularly proud of is the custom filtering logic.  
It allows combining multiple filters dynamically instead of hardcoding conditions.
```java
ArrayList<Transaction> result = transactionList;

result = filterByStartDate(startDateInput, result);
result = filterByEndDate(endDateInput, result);
result = filterByDescription(descriptionInput, result);
result = filterByVendor(vendorInput, result);
result = filterByAmount(amountInput, result);

displayTransactions(result);
```
Each filter works independently, making the logic easy to extend and reuse.
```java
private static ArrayList<Transaction> filterByStartDate(String input, ArrayList<Transaction> list) {
    if (input.isBlank()) return list;

    ArrayList<Transaction> filtered = new ArrayList<>();
    LocalDate startDate = LocalDate.parse(input, DATE_FORMATTER);

    for (Transaction t : list) {
        if (t.getDate().isAfter(startDate)) {
            filtered.add(t);
        }
    }
    return filtered;
}
```
<hr style="height:3px;">

## ⚠️ Challenges I Faced
One of the main challenges was handling date and time formatting correctly, especially when seconds were missing in some cases. I also had to make file writing consistent to avoid formatting issues when saving and reading data. Another part that took some thought was designing filtering logic that is flexible and reusable instead of hardcoding conditions. At the same time, I tried to keep the console navigation simple and intuitive without making the menus feel cluttered.
<hr style="height:3px;">

## 🚀 Future Improvements
- Export reports (CSV / PDF)
- Add categories for expenses
- Monthly budget tracking
- Data visualization (charts)
- Improve error handling and validation
- Add a GUI (JavaFX or web version)
<hr style="height:3px;">


## 👤 Author
**Tim Startsev**

