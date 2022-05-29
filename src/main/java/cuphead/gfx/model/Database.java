package cuphead.gfx.model;

import cuphead.gfx.Main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Database {
    private static final String savedUsersPath = Main.getResource("/cuphead/gfx/data/");//"./data/users.dat";
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
            String filename = Objects.requireNonNull(Main.class.getResource("/cuphead/gfx/data/")) + "users.dat";
            filename = filename.substring(6);
            FileOutputStream fileStream = new FileOutputStream(filename);
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
            InputStream fileStream = Main.class.getResourceAsStream("/cuphead/gfx/data/users.dat");
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
