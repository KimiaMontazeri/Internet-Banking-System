package com.company;
import java.util.Date;

public class Transaction
{
    private int amount;
    private final Date date;

    public Transaction (int amount)
    {
        this.amount = amount;
        date = new Date();
    }

    public Date getDate()
    {
        return date;
    }

    public int getAmount()
    {
        return amount;
    }

    public void print()
    {
        System.out.println("Amount is: " + amount);
        System.out.println("Date is: " + date);
    }
}
