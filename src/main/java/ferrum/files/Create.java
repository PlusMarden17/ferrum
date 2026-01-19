package ferrum.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import ferrum.utils.Logger;
import static ferrum.core.Launcher.GAME_PATH;

public class Create {
    public static void createDirs(String version) {
        try {
            Files.createDirectories(Paths.get(GAME_PATH + "/versions"));
            Files.createDirectories(Paths.get(GAME_PATH + "/libraries"));
            Files.createDirectories(Paths.get(GAME_PATH + "/assets/indexes"));
            Files.createDirectories(Paths.get(GAME_PATH + "/assets/objects"));
            Files.createDirectories(Paths.get(GAME_PATH + "/natives"));

            Files.createDirectories(Paths.get(GAME_PATH + "/versions/" + version));
            Files.createDirectories(Paths.get(GAME_PATH + "/natives/" + version));
        } catch (IOException e) {
            Logger.error("An error occurred while trying to create directories: " + e.getMessage());
        }
    }
}
