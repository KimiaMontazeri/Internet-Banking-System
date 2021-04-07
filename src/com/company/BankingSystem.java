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
        Iterator<User> itr = users.iterator();
        while (itr.hasNext())
        {
            User u = itr.next();
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
        // we have iterated the whole list and none of the if statements were true
        System.out.println("User not found.");
        return null;
    }

    public void removeUser(User user)
    {
        if (user == null)
        {
            System.out.println("User doesn't exist.");
            return;
        }
        Iterator<User> itr = users.iterator();
        while (itr.hasNext())
        {
            User temp = itr.next();
            if (temp.equals(user))
            {
                users.remove(temp);
                System.out.println("User removed.");
                return;
            }
        }
//        if (searchUsers(user))
//        {
//            users.remove(user);
//            System.out.println("User removed.");
//            return;
//        }
        System.out.println("User doesn't exist.");
    }

    public void removeAccount(Account account)
    {
        if (account == null)
        {
            System.out.println("Account doesn't exist.");
            return;
        }
        Iterator<Account> itr = accounts.iterator();
        while (itr.hasNext())
        {
            Account temp = itr.next();
            if (temp.equals(account))
            {
                accounts.remove(temp);
                // remove this account from its owner's list of accounts too
                User user = findUser(account.getID());
                user.removeAccount(account);
                System.out.println("Account removed.");
                return;
            }
        }
        System.out.println("Account doesn't exist.");
    }

    public void displayUsers()
    {
        int i = 1;
        Iterator<User> itr = users.iterator();
        while (itr.hasNext())
        {
            User u = itr.next();
            System.out.print("User " + i + ": ");
            System.out.println(u.getFirstName() + " " + u.getLastname() + " " + u.getID());
            i++;
        }
    }

    public void displayAccounts()
    {
        int i = 1;
        Iterator<Account> itr = accounts.iterator();
        while (itr.hasNext())
        {
            System.out.print("Account " + i + ": ");
            itr.next().printAccountData();
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
        return users.contains(user);
    }

    private boolean searchAccounts(Account account)
    {
        return accounts.contains(account);
    }
}
