package com.company;
import java.util.UUID;
import java.util.ArrayList;

public class Account
{
    private final UUID serial;
    private final String ID;
    private String firstName;
    private String lastName;
    private String type;
    private int balance;
    ArrayList<Transaction> transactionList;

    public Account (String ID, String firstName, String lastName, String type, int balance)
    {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
        this.balance = balance;
        serial = UUID.randomUUID();
        transactionList = new ArrayList<Transaction>();
    }

    public String getID()
    {
        return ID;
    }
    public String getFirstName()
    {
        return firstName;
    }
    public String getLastName()
    {
        return lastName;
    }
    public String getType()
    {
        return type;
    }
    public int getBalance()
    {
        return balance;
    }
    public UUID getSerial()
    {
        return serial;
    }
    public ArrayList getTransactionList()
    {
        return transactionList;
    }

    public boolean updateBalance (int amount)
    {
        // note that amount can be a negative integer
        if (balance + amount < 0)
            return false;

        balance += amount;
        return true;
    }

    public void addTransaction (Transaction transaction)
    {
        transactionList.add(transaction);
        //updateBalance(transaction.getAmount());

        if (updateBalance(transaction.getAmount()))
            transactionList.add(transaction);
    }

    public void printTransactions()
    {
        for (Transaction t : transactionList)
            System.out.println(t);
    }

    public void printAccountData()
    {
        System.out.println(serial + ", " + ID + ", " + firstName + " " + lastName + ", " + balance);
    }
}
