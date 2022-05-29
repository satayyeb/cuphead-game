package cuphead.gfx.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database {
    private static final String savedUsersPath = "./data/users.dat";
    private static List<User> users = new ArrayList<>();
    private static User loggedInUser;
    private static int difficulty;
    private static boolean haveLoad;
    private static Savings savings = new Savings();

    static {
        loadUsers();
    }

    public static void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    public static void removeUser(User user) {
        users.remove(user);
        saveUsers();
    }

    public static User getUser(String username) {
        for (User user : users)
            if (user.getUsername().equals(username))
                return user;
        return null;
    }

    public static void saveUsers() {
        try {
            FileOutputStream fileStream = new FileOutputStream(savedUsersPath);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(users);
            objectStream.close();
            fileStream.close();
        } catch (Exception e) {
            System.out.println("An Error occurred during saving users : ");
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    private static void loadUsers() {
        try {
            FileInputStream fileStream = new FileInputStream(savedUsersPath);
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);
            users = (List<User>) objectStream.readObject();
            fileStream.close();
            objectStream.close();
        } catch (Exception e) {
            System.out.println("Can not load savedUsers :(");
            System.out.println("Error : " + Arrays.toString(e.getStackTrace()));
        }
    }

    public static void setLoggedInUser(User user) {
        if (user != null && !users.contains(user))
            throw new RuntimeException("loggedInUser is not in users");
        loggedInUser = user;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static int getDifficulty() {
        return difficulty;
    }

    public static void setDifficulty(int difficulty) {
        Database.difficulty = difficulty;
    }

    public static void setHaveLoad(boolean status) {
        haveLoad = status;
    }

    public static boolean isHaveLoad() {
        return haveLoad;
    }

    public static Savings getSavings() {
        return savings;
    }

    public static void setSavings(Savings savings) {
        Database.savings = savings;
    }

    public static List<User> getUsers() {
        return users;
    }
}
