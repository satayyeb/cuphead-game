package cuphead.gfx.model;

import java.io.File;
import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private File avatar;
    private int score;

    public User(String username, String password, File avatar) {
        this.username = username;
        this.password = password;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        Database.saveUsers();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        Database.saveUsers();
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
        Database.saveUsers();
    }

    public File getAvatar() {
        return avatar;
    }

    public void setAvatar(File avatar) {
        this.avatar = avatar;
        Database.saveUsers();
    }
}
