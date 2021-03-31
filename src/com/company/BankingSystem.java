package com.company;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class BankingSystem
{
    private ArrayList<User> users;
    private ArrayList<Account> accounts;

    public BankingSystem()
    {
        users = new ArrayList<>();
        accounts = new ArrayList<>();
    }

    public void register(User user)
    {
        if (searchUsers(user))
        {
            System.out.println("User already exists.");
            return;
        }
        addUser(user);

    }

    public void addUser(User user)
    {
        users.add(user);
    }

    public boolean login(String id, String pass)
    {
        for (User u : users)
        {
            if (u.getID().equals(id))
            {
                if (u.getPassword().equals(pass))
                {
                    System.out.println("You are logged in.");
                    return true;
                }
                System.out.println("Wrong password!");
                return false;
            }
        }
        System.out.println("User not found.");
        return false;
    }

    public void removeUser(User user)
    {
        if (searchUsers(user))
        {
            users.remove(user);
            System.out.println("User is removed.");
            return;
        }
        System.out.println("User does not exist.");
    }

    public void displayUsers()
    {
        for (User u : users)
        {
            System.out.println("ID: " + u.getID());
            System.out.println(u.getFirstName() + " " + u.getLastname());
            System.out.println("Password: " + u.getPassword());
        }
    }

    public Account findAccount(UUID serial)
    {
        Account temp;
        Iterator<Account> it = accounts.iterator();
        while (it.hasNext())
        {
            temp = it.next();
            if (temp.getSerial().equals(serial))
                return temp;
        }
        return null;
    }

    private boolean searchUsers(User user)
    {
        for (User u : users)
        {
            // compare the user with the all the existing users by their ID
            if (u.getID().equals(user.getID()))
                return true;
        }
        return false;
    }

    private boolean searchAccounts(Account account)
    {
        for (Account a : accounts)
        {
            if (a.equals(account))
                return true;
        }
        return false;
    }
}
