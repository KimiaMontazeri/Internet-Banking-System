package com.company;
import java.util.ArrayList;
import java.util.UUID;

public class User
{
    private String firstName;
    private String lastname;
    private final String ID;
    private String password;
    ArrayList<Account> accountList;

    public User (String firstName, String lastname, String ID, String password)
    {
        this.firstName = firstName;
        this.lastname = lastname;
        this.ID = ID;
        this.password = password;
        accountList = new ArrayList<Account>();
    }

    public String getFirstName()
    {
        return firstName;
    }
    public String getLastname()
    {
        return lastname;
    }
    public String getID()
    {
        return ID;
    }
    public String getPassword()
    {
        return password;
    }

    public void addAccount (Account account)
    {
        // add a search method to check if the account is new or already exists
        // if the given account is new, tun the following code
        accountList.add(account);
    }

    public void removeAccount (Account account)
    {
        // add a search method to check if the given account exists (check if the list is empty)
        // if the given account exists, run the following code
        accountList.remove(account);
    }

    public void deposit (Account account, int amount)
    {
        if (amount < 0)
            amount *= -1;

        Transaction transaction = new Transaction(amount);
        account.updateBalance(amount);
        account.addTransaction(transaction);
        System.out.println("Completed.");
    }

    public void withdrawal (Account account, int amount)
    {
        if (amount > 0)
            amount *= -1;

        if (account.updateBalance(amount))
        {
            Transaction transaction = new Transaction(amount);
            account.addTransaction(transaction);
            System.out.println("Completed.");
            return;
        }
        System.out.println("Not enough money.");
    }

    public void transfer (Account srcAccount, Account destAccount, int amount)
    {
        // assume amount to be a positive integer
        if (amount < 0)
            amount *= -1;

        if (searchAccountsBySerial(destAccount.getSerial()))
        {
            if (srcAccount.updateBalance((-1) * amount))
            {
                destAccount.updateBalance(amount);
                Transaction transaction = new Transaction(amount);
                srcAccount.addTransaction(transaction);
                destAccount.addTransaction(transaction);
                System.out.println("Completed.");
                return;
            }
        }
        System.out.println("Destination account doesn't exist or there is not enough money " +
                "in your account.");
    }

    public void checkBalance (Account account)
    {
        System.out.println(account.getBalance());
    }

    public void printAllAvailableAccounts()
    {
        int i = 1;
        for (Account a : accountList)
        {
            System.out.print("Account " + i + ": ");
            a.printAccountData();
            i++;
        }
    }

    public void printUserData()
    {
        System.out.println(firstName + " " + lastname);
        System.out.println(ID);
        System.out.println(password);
    }

//    private boolean searchAccounts(Account account)
//    {
//        for (Account a : accountList)
//        {
//            if (a.equals(account))
//                return true;
//        }
//        return false;
//    }

    // the user types : " destAccountSerial amount " for transferring money
    // the program uses this method to check if the destination account exists
    private boolean searchAccountsBySerial (UUID serial)
    {
        for (Account a : accountList)
        {
            if (a.getSerial().equals(serial))
                return true;
        }
        return false;
    }


}
