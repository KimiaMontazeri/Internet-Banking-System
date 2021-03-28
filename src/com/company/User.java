package com.company;
import java.util.ArrayList;

public class User
{
    private String firstName;
    private String lastname;
    private String ID;
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
        // search if the account is valid
        if (amount <= 0)
        {
            System.out.println("The amount of money to deposit is not valid!");
            return;
        }
        Transaction transaction = new Transaction(amount);
        account.addTransaction(transaction);
    }

    public void withdrawal (Account account, int amount)
    {
        // search if the account is valid
        // amount should be negative because we're withdrawing money
        if (amount >= 0)
        {
            System.out.println("The amount of money to withdraw is not valid!");
            return;
        }
        Transaction transaction = new Transaction(amount);
        account.addTransaction(transaction);
    }

    public void transfer (Account srcAccount, Account destAccount, int amount)
    {
        // search if the accounts exist
        // assume amount to be a positive integer
        if (amount < 0)
            amount *= -1;

        Transaction transaction = new Transaction(amount);
        if (srcAccount.updateBalance((-1) * amount))
        {
            destAccount.updateBalance(amount);
            srcAccount.addTransaction(transaction);
            destAccount.addTransaction(transaction);
        }
    }

    public void checkBalance (Account account)
    {
        System.out.println(account.getBalance());
    }

    public void printAllAvailableAccounts()
    {
        for (Account a : accountList)
            a.printAccountData();
    }

    public void printUserData()
    {
        System.out.println(firstName + " " + lastname);
        System.out.println(ID);
        System.out.println(password);
    }


}
