package com.company;
import java.util.Scanner;
import java.util.UUID;

public class Main
{
    private static BankingSystem bank;
    private static User currentUser;
    private static Account currentAccount;

    public static void signUp()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1.Go to sign in menu\n2.Back");
        if (scanner.nextLine().equals("2"))
            return;

        System.out.println("Enter info: (firstname, lastname, ID, password)");

        String firstname = scanner.nextLine();
        String lastname = scanner.nextLine();
        String ID = scanner.nextLine();
        String password = scanner.nextLine();

        User newUser = new User(firstname, lastname, ID, password);
        if (bank.register(newUser))
            return;

        System.out.println("1.Try again\n2.Back");
        if (scanner.nextLine().equals("1"))
            signUp();
    }

    public static void logIn()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1.Go to log in menu\n2.Back");
        if (scanner.nextLine().equals("2"))
            return;

        System.out.println("Enter ID and password:");
        String ID = scanner.nextLine();
        String password = scanner.nextLine();

        currentUser = bank.login(ID, password);
        if (currentUser != null)
            loggedInUserMenu();
    }

    public static void loggedInUserMenu()
    {
        Scanner scanner = new Scanner(System.in);
        String userChoice;

        do {
            System.out.println("1.Existing accounts\n2.Add new account\n3.Log out");
            userChoice = scanner.nextLine();
            switch (userChoice)
            {
                case "1" -> existingAccounts();
                case "2" -> addNewAccount();
                case "3" -> {
                    currentUser = null;
                    System.out.println("Logged out of user.");
                }
            }
        }while (!userChoice.equals("3"));
    }

    public static void addNewAccount()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your user ID and the account type:");
        String ID = scanner.nextLine();
        String type = scanner.nextLine();
        if (ID.equals(currentUser.getID()))
        {
            Account account = new Account(ID, currentUser.getFirstName(), currentUser.getLastname(), type);
            currentUser.addAccount(account);
            bank.addAccount(account);
        }
        else System.out.println("Invalid ID!");
    }

    public static void existingAccounts()
    {
        Scanner scanner = new Scanner(System.in);
        currentUser.printAllAvailableAccounts();

        System.out.println("Enter the chosen account number or enter 0 to go back:");
        int index = scanner.nextInt() - 1;
        if (index == -1)
            return;

        currentAccount = currentUser.getAccountByIndex(index);
        while (currentAccount == null)
        {
            System.out.println("Invalid input.");
            System.out.println("1.Try again\n2.Back");
            if (scanner.nextLine().equals("1"))
                currentAccount = currentUser.getAccountByIndex(scanner.nextInt() - 1);
            else
                return;
        }
        System.out.println("Logged into account.");
        loggedInAccountMenu();
    }

    public static void loggedInAccountMenu()
    {
        Scanner scanner = new Scanner(System.in);
        String userChoice;
        do {
            System.out.println("1.Withdrawal\n2.Deposit\n3.Transfer\n4.Check balance\n5.Print transactions\n6.Back");
            userChoice = scanner.nextLine();
            switch (userChoice)
            {
                case "1" -> withdrawal(getAmount());
                case "2" -> deposit(getAmount());
                case "3" -> transfer();
                case "4" -> currentUser.checkBalance(currentAccount);
                case "5" -> {
                    currentAccount.printAccountData();
                    System.out.println();
                    currentAccount.printTransactions();
                    System.out.println();
                }
                case "6" -> currentAccount = null;
            }
        }while(!userChoice.equals("6"));
    }

    public static int getAmount()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("How much money?");
        return Integer.parseInt(scanner.nextLine());
    }

    public static void withdrawal(int amount)
    {
        currentUser.withdrawal(currentAccount, amount);
    }

    public static void deposit(int amount)
    {
        currentUser.deposit(currentAccount, amount);
    }

    public static void transfer()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter destination account serial number and " +
                "the amount of money to transfer(serial amount)");

        String[] result = scanner.nextLine().split("\\s");
        // check if result[0] is valid to be converted into uuid in order to avoid ILLEGAL ARGUMENT EXCEPTION
        if (validUuid(result[0]))
        {
            UUID serial = UUID.fromString(result[0]);
            int amount = Integer.parseInt(result[1]);

            Account destAccount = bank.findAccount(serial);
            if (!currentUser.transfer(currentAccount, destAccount, amount))
                System.out.println("Destination account doesn't exist or there is not enough money in your account.");
        }
        System.out.println("Invalid input for serial number.");
    }

    public static boolean validUuid(String str)
    {
        String[] result = str.split("-");
        return result.length == 5;
    }

    public static void sysadminLogIn()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1.Log in as sysadmin\n2.Back");
        if (scanner.nextLine().equals("2"))
            return;

        System.out.println("Enter userID and password");
        String username = scanner.nextLine();
        String password = scanner.nextLine();
        if (username.equals("sysadmin") && password.equals("1234"))
        {
            System.out.println("Logged in as sysadmin.");
            sysadminMenu();
        }
        else
            System.out.println("Username or password is incorrect.");
    }

    public static void sysadminMenu()
    {
        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            System.out.println("1.Display users\n2.Display accounts\n3.Remove user\n4.Remove account\n5.Back");
            choice = scanner.nextLine();
            switch (choice)
            {
                case "1" -> bank.displayUsers();
                case "2" -> bank.displayAccounts();
                case "3" -> removeUser();
                case "4" -> removeAccount();
            }
        }while (!choice.equals("5"));
    }

    public static void removeUser()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the user's ID:");
        String ID = scanner.nextLine();
        User user = bank.findUser(ID);
        bank.removeUser(user);
    }

    public static void removeAccount()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the account's serial number:");
        String str = scanner.nextLine();
        if (validUuid(str))
        {
            UUID serial = UUID.fromString(str);
            Account account = bank.findAccount(serial);
            // removes the account from both the bank's list of account and the owner's list of account
            bank.removeAccount(account);
        }
        System.out.println("Invalid input for a serial number.");
    }


    public static void main(String[] args)
    {
        bank = new BankingSystem();
        currentUser = null;
        currentAccount = null;

        // main menu
        String userChoice;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("1.Sign up");
            System.out.println("2.Log in");
            System.out.println("3.System admin");
            System.out.println("4.Exit");
            userChoice = scanner.nextLine();

            switch (userChoice) {
                case "1" -> signUp();
                case "2" -> logIn();
                case "3" -> sysadminLogIn();
                case "4" -> System.out.println("Bye :))");
                default -> System.out.println("Invalid input.");
            }
        }while(!userChoice.equals("4"));
    }
}
