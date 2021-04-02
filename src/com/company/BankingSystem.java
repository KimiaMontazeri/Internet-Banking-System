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

    public ArrayList<User> getUsers()
    {
        return users;
    }

    public ArrayList<Account> getAccounts()
    {
        return accounts;
    }

    public boolean register(User user)
    {
        if (searchUsers(user))
        {
            System.out.println("user already exists.");
            return false;
        }
        addUser(user);
        System.out.println("user created.");
        return true;
    }

    public void addUser(User user)
    {
        users.add(user);
    }

    public void addAccount(Account account)
    {
        accounts.add(account);
        System.out.println("New account opened.");
    }

    public User login(String id, String pass)
    {
        for (User u : users)
        {
            if (u.getID().equals(id))
            {
                if (u.getPassword().equals(pass))
                {
                    System.out.println("Logged in.");
                    return u;
                }
                System.out.println("Wrong password!");
                return null;
            }
        }
        System.out.println("User not found.");
        //System.out.println("user doesn't exists or password is incorrect.");
        return null;
    }

    public boolean removeUser(User user)
    {
        if (user == null)
        {
            System.out.println("User doesn't exist.");
            return false;
        }
        if (searchUsers(user))
        {
            users.remove(user);
            System.out.println("User removed.");
            return true;
        }
        System.out.println("User doesn't exist.");
        return false;
    }

    public boolean removeAccount(Account account)
    {
        if (account == null)
        {
            System.out.println("Account doesn't exist.");
            return false;
        }
        if (searchAccounts(account))
        {
            // remove from the bank's list of accounts
            accounts.remove(account);
            // remove the account from the user's list of accounts
            User user = findUser(account.getID());
            user.removeAccount(account);
            System.out.println("Account removed.");
            return true;
        }
        System.out.println("Account doesn't exist.");
        return false;
    }

    public void displayUsers()
    {
        int i = 1;
        for (User u : users)
        {
            System.out.print("User " + i + ": ");
            System.out.println(u.getFirstName() + " " + u.getLastname() + " " + u.getID());
            i++;
        }
    }

    public void displayAccounts()
    {
        int i = 1;
        for (Account a : accounts)
        {
            System.out.print("Account " + i + ": ");
            a.printAccountData();
            i++;
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

    public User findUser(String ID)
    {
        User temp;
        Iterator<User> it = users.iterator();
        while (it.hasNext())
        {
            temp = it.next();
            if (temp.getID().equals(ID))
                return temp;
        }
        return null;
    }

    private boolean searchUsers(User user)
    {
//        for (User u : users)
//        {
//            // compare the user with all the existing users by their ID
//            if (u.getID().equals(user.getID()))
//                return true;
//        }
        return users.contains(user);
    }

    private boolean searchAccounts(Account account)
    {
//        for (Account a : accounts)
//        {
//            if (a.equals(account))
//                return true;
//        }
        return accounts.contains(account);
    }
}
