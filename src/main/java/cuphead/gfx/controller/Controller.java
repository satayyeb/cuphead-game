package cuphead.gfx.controller;

import cuphead.gfx.Main;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Controller {
    public static boolean isPasswordWeak(String password) {
        if (password.length() == 0) return false;
        return password.length() < 5;
    }

    public static List<String> getPNGFilesInDir(String path) {
        List<String> files = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            try {
                String filename = path + i + ".png";
                String resource = Main.getResource(filename);
                if (!resource.isEmpty())
                    files.add(filename);
            } catch (Exception ignore) {
            }
        }
        files.removeIf(filename -> !filename.endsWith(".png"));
        files.sort(Comparator.comparing(filename -> Integer.parseInt(filename.replaceAll("^.+/(\\d+)\\.png$", "$1"))));
        return files;
    }

}
