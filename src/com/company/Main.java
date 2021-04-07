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
        // if the user was new and didn't exist in the list of all the users
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

        currentUser = bank.login(ID, password); // returns null if the user has entered wrong ID or password
        if (currentUser != null)
            loggedInUserMenu();
    }

    public static void loggedInUserMenu()
    {
        Scanner scanner = new Scanner(System.in);
        String userChoice;

        do {
            System.out.println("1.Existing accounts\n2.Add new account\n3.Log out and go to main menu");
            userChoice = scanner.nextLine();
            switch (userChoice) {
                case "1" -> {
                    if (existingAccounts() == 1)
                        return;
                }
                case "2" -> {
                    if (addNewAccount() == 1)
                        return;
                }
                case "3" -> {
                    currentUser = null;
                    System.out.println("Logged out of user.");
                }
            }
        } while (!userChoice.equals("3"));
    }

    // returns 1 if the user has wanted to go to the main menu,
    // in this case, we have to return every previous method that's been called and go back to main
    public static int addNewAccount()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your user ID and the account type or enter 0 to go to the main menu:");
        String ID = scanner.nextLine();
        if (ID.equals("0")) // go back to main menu
        {
            currentUser = null;
            return 1;
        }
        String type = scanner.nextLine();
        if (ID.equals(currentUser.getID())) // check if the user has entered the right ID
        {
            Account account = new Account(ID, currentUser.getFirstName(), currentUser.getLastname(), type);
            currentUser.addAccount(account);
            bank.addAccount(account);
        }
        else System.out.println("Invalid ID!");
        return 0;
    }

    // returns 1 if the user has wanted to go to the main menu,
    // in this case, we have to return every previous method that's been called and go back to main
    public static int existingAccounts()
    {
        Scanner scanner = new Scanner(System.in);
        currentUser.printAllAvailableAccounts();

        System.out.println("Enter the chosen account number\nEnter 0 to go back\nEnter -1 to go to main menu:");
        int index = scanner.nextInt() - 1;
        if (index == -1)
            return 0; // goes back to loggedInUserMenu
        if (index == -2)
            return 1; // goes back to main menu

        currentAccount = currentUser.getAccountByIndex(index);
        while (currentAccount == null)
        {
            System.out.println("Invalid input.");
            System.out.println("1.Try again\n2.Back");
            if (scanner.nextLine().equals("1"))
                currentAccount = currentUser.getAccountByIndex(scanner.nextInt() - 1);
            else
                return 0;
        }
        System.out.println("Logged into account.");
        if (loggedInAccountMenu() == 1) // user chose to go to the main menu
            return 1;
        return 0;
    }

    // returns 1 if the user has wanted to go to the main menu,
    // in this case, we have to return every previous method that's been called and go back to main
    public static int loggedInAccountMenu()
    {
        Scanner scanner = new Scanner(System.in);
        String userChoice;
        do {
            System.out.println("1.Withdrawal\n2.Deposit\n3.Transfer\n4.Check balance\n5.Print transactions\n6.Back\n7.Main Menu");
            userChoice = scanner.nextLine();
            switch (userChoice)
            {
                case "1" -> withdrawal(getAmount());
                case "2" -> deposit(getAmount());
                case "3" -> {
                    if (transfer() == 1) // user chose to go to the main menu
                        return 1;
                }
                case "4" -> currentUser.checkBalance(currentAccount);
                case "5" -> {
                    System.out.println();
                    currentAccount.printTransactions();
                    System.out.println();
                }
                case "6" -> currentAccount = null;
                case "7" -> {
                    currentUser = null;
                    currentAccount = null;
                    return 1;
                }
            }
        }while(!userChoice.equals("6"));
        return 0;
    }

    // get the amount of money from the console and check its validity
    public static Integer getAmount()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("How much money?");
        return validAmount(scanner.nextLine());
    }

    // check if the input string is valid to be converted into integer
    public static Integer validAmount(String amount)
    {
        int num;
        try
        {
            num = Integer.parseInt(amount);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
        return num;
    }

    public static boolean validUuid(String str)
    {
        String[] result = str.split("-");
        return result.length == 5;
    }

    public static void withdrawal(Integer amount)
    {
        if (amount == null) // check if the amount of money is valid
        {
            System.out.println("Invalid input for amount of money.");
            return;
        }
        currentUser.withdrawal(currentAccount, amount);
    }

    public static void deposit(Integer amount)
    {
        if (amount == null) // check if the amount of money is valid
        {
            System.out.println("Invalid input for amount of money.");
            return;
        }
        currentUser.deposit(currentAccount, amount);
    }

    // returns 1 if the user has wanted to go to the main menu,
    // in this case, we have to return every previous method that's been called and go back to main
    public static int transfer()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter destination account serial number and " +
                "the amount of money to transfer or enter 0 to go back to the main menu");

        String[] result = scanner.nextLine().split("\\s"); // split the input string based on whitespaces
        if (result[0].equals("0")) // user has entered 0 to go to the main menu
        {
            currentUser = null;
            currentAccount = null;
            return 1;
        }
        Integer amount = validAmount(result[1]);
        // check if result[0] is valid to be converted into uuid in order to avoid ILLEGAL ARGUMENT EXCEPTION
        if (validUuid(result[0]) && amount != null)
        {
            UUID serial = UUID.fromString(result[0]);
            Account destAccount = bank.findAccount(serial);
            if (!currentUser.transfer(currentAccount, destAccount, amount))
                System.out.println("Destination account doesn't exist or there is not enough money in your account.");
            return 0;
        }
        System.out.println("Invalid input for serial number or amount of money.");
        return 0;
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
            System.out.println("1.Display users\n2.Display accounts\n3.Remove user\n4.Remove account\n5.Log out");
            choice = scanner.nextLine();
            switch (choice)
            {
                case "1" -> bank.displayUsers();
                case "2" -> bank.displayAccounts();
                case "3" -> {
                    if (removeUser() == 1) // user wants to go to the main menu
                        return;
                }
                case "4" -> {
                    if (removeAccount() == 1) // user wants to go to the main menu
                        return;
                }
            }
        }while (!choice.equals("5"));
    }

    // returns 1 if the user has wanted to go to the main menu,
    // in this case, we have to return every previous method that's been called and go back to main
    public static int removeUser()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the user's ID or enter 0 to go back to the main menu:");
        String ID = scanner.nextLine();
        if (ID.equals("0"))
            return 1; // go back to main menu
        User user = bank.findUser(ID);
        bank.removeUser(user);
        return 0;
    }

    // returns 1 if the user has wanted to go to the main menu,
    // in this case, we have to return every previous method that's been called and go back to main
    public static int removeAccount()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the account's serial number or enter 0 to go back to the main menu:");
        String str = scanner.nextLine();
        if (str.equals("0"))
            return 1; // go back to main menu
        if (validUuid(str)) // check if the input string is valid to be converted into uuid
        {
            UUID serial = UUID.fromString(str);
            Account account = bank.findAccount(serial);
            // the method removes the account from both the bank's list of account and the owner's list of account
            bank.removeAccount(account);
            return 0;
        }
        System.out.println("Invalid input for a serial number.");
        return 0;
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
            System.out.println("1.Sign up\n2.Log in\n3.System admin\n4.Exit");
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
