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

    public Account getAccountByIndex(int index)
    {
        if (index < 0 || index >= accountList.size())
            return null;
        return accountList.get(index);
    }

    public boolean addAccount (Account account)
    {
        // check if the account already exists
        if (searchAccounts(account))
        {
            System.out.println("This account already exists.");
            return false;
        }
        accountList.add(account);
        System.out.println("New account opened.");
        return true;
    }

    public boolean removeAccount (Account account)
    {
        // add a search method to check if the given account exists (check if the list is empty)
        // if the given account exists, run the following code
        accountList.remove(account);
        if (searchAccounts(account))
        {
            accountList.remove(account);
            return true;
        }
        return false;
    }

    public boolean deposit (Account account, int amount)
    {
        if (!searchAccounts(account))
        {
            System.out.println("This account does not exist in the list.");
            return false;
        }

        if (amount < 0)
            amount *= -1;

        Transaction transaction = new Transaction(amount);
        account.updateBalance(amount);
        account.addTransaction(transaction);
        System.out.println("Completed.");
        return true;
    }

    public boolean withdrawal (Account account, int amount)
    {
        if (!searchAccounts(account))
        {
            System.out.println("This account does not exist in the list.");
            return false;
        }

        if (amount > 0)
            amount *= -1;

        // check if there's enough money
        if (account.updateBalance(amount))
        {
            Transaction transaction = new Transaction(amount);
            account.addTransaction(transaction);
            System.out.println("Completed.");
            return true;
        }
        System.out.println("Not enough money.");
        return false;
    }

    public boolean transfer (Account srcAccount, Account destAccount, int amount)
    {
        if (!searchAccounts(srcAccount))
        {
            System.out.println("The source account does not exist in the list.");
            return false;
        }

        if (amount < 0)
            amount *= -1;

        if (searchAccounts(destAccount))
        {
            if (srcAccount.updateBalance((-1) * amount))
            {
                destAccount.updateBalance(amount);

                Transaction transaction1 = new Transaction((-1) * amount);
                Transaction transaction2 = new Transaction(amount);

                srcAccount.addTransaction(transaction1);
                destAccount.addTransaction(transaction2);

                System.out.println("Completed.");
                return true;
            }
        }
        System.out.println("Destination account doesn't exist or there is not enough money " +
                "in your account.");
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
//        for (Account a : accountList)
//        {
//            if (a.equals(account))
//                return true;
//        }
        return accountList.contains(account);
    }

    // the user types : " destAccountSerial amount " for transferring money
    // the program uses this method to check if the destination account exists
//    private boolean searchAccountsBySerial (UUID serial) //it may be static idk
//    {
//        for (Account a : accountList)
//        {
//            if (a.getSerial().equals(serial))
//                return true;
//        }
//        return false;
//    }


}
