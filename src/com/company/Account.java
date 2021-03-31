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
    private ArrayList<Transaction> transactionList;

    public Account (String ID, String firstName, String lastName, String type)
    {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
        balance = 0;
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
    public ArrayList<Transaction> getTransactionList()
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

    public void updateBalanceBetter(int amount)
    {
        if (hasEnoughMoney(amount))
            balance += amount;
    }

    public boolean hasEnoughMoney(int amount)
    {
        return balance + amount >= 0;
    }

    public void addTransaction (Transaction transaction)
    {
        if (hasEnoughMoney(transaction.getAmount()))
        {
            transactionList.add(transaction);
            //updateBalance(transaction.getAmount());
        }
    }

    public void printTransactions()
    {
        for (Transaction t : transactionList)
            t.print();
    }

    public void printAccountData()
    {
        System.out.println(serial + ", " + type + ", " + ID + ", " + firstName + " " + lastName + ", " + balance);
    }
}
