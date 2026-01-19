package ferrum.utils;

import ferrum.core.Launcher;
import ferrum.core.Profile;
import ferrum.core.User;
import org.json.JSONObject;

public class Command {
    public static void buildCommand(Profile profile, User user,  String classpath, JSONObject versionInfo, String assetIndexId) {
        try {
            String mainClass = versionInfo.getString("mainClass");
            String nativesDir = Launcher.GAME_PATH + "/natives/" + profile.getVersion();

            ProcessBuilder pb = new ProcessBuilder(
                    "java",
                    Formatter.formatMin(profile.getMinMem()),
                    Formatter.formatMax(profile.getMaxMem()),
                    "-Djava.library.path=" + nativesDir,
                    "-cp", classpath,
                    mainClass,
                    "--username", user.getUsername(),
                    "--version", profile.getVersion(),
                    "--gameDir", Launcher.GAME_PATH,
                    "--assetsDir", Launcher.GAME_PATH + "/assets",
                    "--assetIndex", assetIndexId,
                    "--uuid", "00000000-0000-0000-0000-000000000000",
                    "--accessToken", "0",
                    "--userType", "legacy",
                    "--versionType", profile.getVersionType().getSecondName()
            );

            Process process = pb.start();
            process.waitFor();
        } catch (Exception e) {
            Logger.error("An error occurred while trying to launch the game: " + e.getMessage());
        }
    }
}
