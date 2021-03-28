package com.company;
import java.util.UUID;
import java.util.ArrayList;

public class Account
{
    private final UUID uuid;
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
        uuid = UUID.randomUUID();
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
    public UUID getUuid()
    {
        return uuid;
    }
    public ArrayList getTransactionList()
    {
        return transactionList;
    }

    public boolean updateBalance (int amount)
    {
        // note that amount can be a negative integer
        // if statement is true when we deposit too much money from the account
        if (balance + amount < 0)
        {
            System.out.println("Not enough money!");
            return false;
        }
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
        System.out.println("Info of the account's owner is:");
        System.out.println("ID: " + ID);
        System.out.println("Firstname: " + firstName);
        System.out.println("Lastname: " + lastName);
        System.out.println();
        System.out.println("Account type: " + type);
        System.out.println("Account balance: " + balance);
        System.out.println();
        System.out.println("List of all the transactions: ");
        printTransactions();
    }
}
