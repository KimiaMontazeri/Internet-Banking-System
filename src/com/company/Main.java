package com.company;
import javax.swing.*;
import java.util.Scanner;
import java.util.UUID;

public class Main
{
    User currentUser;
    Account currentAccount;
    boolean sysadmin;

    public boolean signUp(BankingSystem bank)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1.Continue\n2.Back");
        if (scanner.nextInt() == 2)
            return false;

        System.out.println("Enter info: (firstname, lastname, ID, password)");

        String firstname = scanner.nextLine();
        String lastname = scanner.nextLine();
        String ID = scanner.nextLine();
        String password = scanner.nextLine();

        User newUser = new User(firstname, lastname, ID, password);
        return bank.register(newUser);
    }

    public boolean logIn(BankingSystem bank)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1.Continue\n2.Back");
        if (scanner.nextInt() == 2)
            return false;

        System.out.println("Enter ID and password:");
        String ID = scanner.nextLine();
        String password = scanner.nextLine();

        currentUser = bank.login(ID, password);
        return currentUser != null;
    }

    public void loggedInUserMenu(BankingSystem bank)
    {
        // check if we're logged in
        if (currentUser == null)
            return;

        Scanner scanner = new Scanner(System.in);
        System.out.println("1.Existing accounts\n2.Add new account\n3.Log out");

        if (scanner.nextInt() == 1)
            existingAccounts();
        if (scanner.nextInt() == 2)
        {
            System.out.println("Enter your user ID and the account type:");
            String ID = scanner.nextLine();
            String type = scanner.nextLine();

            Account account = new Account(ID, currentUser.getFirstName(), currentUser.getLastname(), type);
            System.out.println("New account opened.");
            bank.addAccount(account);
        }
        if (scanner.nextInt() == 2)
        {
            System.out.println("Logged out of user");
            currentUser = null;
        }
    }

    public void existingAccounts()
    {
        Scanner scanner = new Scanner(System.in);
        currentUser.printAllAvailableAccounts();
        // get the account by the scanned account number
        currentAccount = currentUser.getAccountByIndex(scanner.nextInt());
        while (currentAccount == null)
        {
            System.out.println("Invalid input. Try again:");
            currentAccount = currentUser.getAccountByIndex(scanner.nextInt());
        }
        System.out.println("Logged into account.");
    }

    public void loggedInAccountMenu(BankingSystem bank)
    {
        // check if we're logged in
        if (currentUser == null)
        {
            System.out.println("You haven't logged into a user yet.");
            logIn(bank);
        }
        if (currentAccount == null)
        {
            System.out.println("You haven't logged into an account yet.");
            existingAccounts();
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("1.Withdrawal\n2.Deposit\n3.Transfer\n4.Check balance\n5.Back");
        int choice = scanner.nextInt();
        if (choice == 1 || choice == 2)
        {
            withdrawalOrDeposit(bank);
            return;
        }
        if (choice == 3)
        {
            transfer(bank);
            return;
        }
        if (scanner.nextInt() == 4)
        {
            currentUser.checkBalance(currentAccount);
            return;
        }
        if (scanner.nextInt() == 5)
            logIn(bank);
    }

    public void withdrawalOrDeposit(BankingSystem bank)
    {
        Scanner scanner = new Scanner(System.in);
        int amount;
        System.out.println("How much money?");
        amount = scanner.nextInt();
        currentUser.withdrawal(currentAccount, amount);

        // go back
        loggedInAccountMenu(bank);
    }

    public void transfer (BankingSystem bank)
    {
        Scanner scanner = new Scanner(System.in);
        int amount;
        System.out.println("How much money?");
        amount = scanner.nextInt();

        // destAccount serial amount -> u have to scan this and concatenate by whitespace
        System.out.println("Destination account serial number?");
        UUID serial = UUID.fromString(scanner.next());
        Account destAccount = bank.findAccount(serial);
        currentUser.transfer(currentAccount, destAccount, amount);

        // go back
        loggedInAccountMenu(bank);
    }

    public void sysadminLogIn(BankingSystem bank)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1.Continue\n2.Back");
        if (scanner.nextInt() == 2)
            return;

        System.out.println("Enter userId and password");
        String username = scanner.nextLine();
        String password = scanner.nextLine();
        if (username.equals("sysadmin") && password.equals("1234"))
        {
            System.out.println("Logged in as sysadmin.");
            sysadmin = true;
            sysadminMenu(bank);
        }
        System.out.println("Username or password is incorrect.");
    }

    public void sysadminMenu(BankingSystem bank)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1.Display users\n2.Display account\n3.Remove user\n4.Remove account\n5.Back");
        if (scanner.nextInt() == 1)
        {
            bank.displayUsers();
            return;
        }
        if (scanner.nextInt() == 2)
        {
            bank.displayAccounts();
            return;
        }
        if (scanner.nextInt() == 3)
        {
            removeUser(bank);
            return;
        }
        if (scanner.nextInt() == 4)
        {
            removeAccount(bank);
            return;
        }
        if (scanner.nextInt() == 5)
            sysadminLogIn(bank);

    }

    public void removeUser(BankingSystem bank)
    {
        if (!sysadmin)
        {
            System.out.println("System admin has not logged in yet.");
            sysadminLogIn(bank);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the user's ID:");
        String ID = scanner.nextLine();
        User user = bank.findUser(ID);

        if (user == null)
        {
            System.out.println("User doesn't exist.");
            sysadminMenu(bank);
        }
        bank.removeUser(user);
        System.out.println("User removed.");
        sysadminMenu(bank);
    }

    public void removeAccount(BankingSystem bank)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the account's serial number:");
        UUID serial = UUID.fromString(scanner.nextLine());
        Account account = bank.findAccount(serial);
        if (account == null)
        {
            System.out.println("Account doesn't exist.");
            sysadminMenu(bank);
        }
        bank.removeAccount(account);

        // remove the account from the user's list of accounts too
        String ID = account.getID();
        User user = bank.findUser(ID);
        user.removeAccount(account);

        System.out.println("Account removed.");
        sysadminMenu(bank);
    }

    public void main(String[] args)
    {
        BankingSystem bank = new BankingSystem();
        currentUser = null;
        currentAccount = null;

        int userChoice;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("1.Sign up");
            System.out.println("2.Log in");
            System.out.println("3.System admin");
            System.out.println("4.Exit");
            userChoice = scanner.nextInt();

            switch (userChoice)
            {
                case 1:
                    while(!signUp(bank))
                    {
                        System.out.println("1.Try again\n2.Back");
                        if(scanner.nextInt() == 2)
                            break;
                    }
                    break;
                case 2:
                    while(!logIn(bank))
                    {
                        System.out.println("1.Try again\n2.Back");
                        if (scanner.nextInt() == 2)
                            break;
                    }
                    break;
                case 3:
                    sysadminLogIn(bank);
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }while(userChoice != 4);
    }
}
