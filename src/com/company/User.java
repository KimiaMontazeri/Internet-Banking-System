package com.company;
import java.util.ArrayList;

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

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public Account getAccountByIndex(int index)
    {
        if (index < 0 || index >= accountList.size()) // check if the index is not out of boundary
            return null;
        return accountList.get(index);
    }

    public void addAccount (Account account)
    {
        // check if the account already exists
        if (searchAccounts(account))
        {
            System.out.println("This account already exists.");
            return;
        }
        accountList.add(account);
    }

    public void removeAccount (Account account)
    {
        if (searchAccounts(account)) // check if the account exists
            accountList.remove(account);
    }

    public void deposit (Account account, int amount)
    {
        if (searchAccounts(account)) // check if the account exists
        {
            if (amount < 0)
                amount *= -1;

            Transaction transaction = new Transaction(amount);
            account.updateBalance(amount);
            account.addTransaction(transaction);
            System.out.println("Completed.");
            return;
        }
        System.out.println("This account does not exist in the list.");
    }

    public void withdrawal (Account account, int amount)
    {
        if (searchAccounts(account)) // check if the account exists
        {
            if (amount > 0)
                amount *= -1;

            // check if there's enough money
            if (account.updateBalance(amount))
            {
                Transaction transaction = new Transaction(amount);
                account.addTransaction(transaction);
                System.out.println("Completed.");
                return;
            }
            System.out.println("Not enough money.");
            return;
        }
        System.out.println("This account does not exist in the list.");
    }

    public boolean transfer (Account srcAccount, Account destAccount, int amount)
    {
        if (!searchAccounts(srcAccount) || destAccount == null) // the accounts don't exist
            return false;

        if (amount < 0)
            amount *= -1;

        if (srcAccount.updateBalance((-1) * amount)) // check if the source account has enough money
        {
            destAccount.updateBalance(amount);

            // creates a new object of transaction class
            Transaction transaction1 = new Transaction((-1) * amount);
            Transaction transaction2 = new Transaction(amount);

            srcAccount.addTransaction(transaction1);
            destAccount.addTransaction(transaction2);

            System.out.println("Completed.");
            return true;
        }
        // returns false if there's not enough money in the source account
        return false;
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

    private boolean searchAccounts(Account account)
    {
        for (Account a : accountList)
        {
            if (a.equals(account))
                return true;
        }
        return false;
//        return accountList.contains(account);
    }
}
