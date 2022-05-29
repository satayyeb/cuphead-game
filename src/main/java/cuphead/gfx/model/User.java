package cuphead.gfx.model;

import java.io.File;
import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String avatarPath;
    private int score;

    public User(String username, String password, String avatarPath) {
        this.username = username;
        this.password = password;
        this.avatarPath = avatarPath;
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

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
        Database.saveUsers();
    }
}
