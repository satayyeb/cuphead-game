package cuphead.gfx.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Controller {
    public static boolean isPasswordWeak(String password) {
        if (password.length() == 0)
            return false;
        return password.length() < 5;
    }

    public static List<File> getPNGFilesInDir(String directory) {
        List<File> files = new ArrayList<>(List.of(Objects.requireNonNull(new File(directory).listFiles())));
        files.removeIf(file -> !file.getName().endsWith(".png"));
        files.sort(Comparator.comparing(file -> Integer.parseInt(file.getName().replaceFirst("\\.[^.]+$", ""))));
        return files;
    }
}
