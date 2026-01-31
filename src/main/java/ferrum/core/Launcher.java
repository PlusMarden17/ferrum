package ferrum.core;

import ferrum.files.Create;
import ferrum.files.Download;
import ferrum.utils.Command;
import ferrum.types.Log;
import ferrum.utils.Logger;

import org.json.JSONObject;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Launcher {

    public static final String VERSION_LIST = "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    public static final String GAME_PATH = System.getProperty("user.home") + "/.Fminecraft";
    
    public static Log defaultLogType = Log.VERBOSE;

    public void muteLogs(boolean option) {
        if (option) {
            defaultLogType = Log.MUTED;
        } else {
            defaultLogType = Log.VERBOSE;
        }
    }

    public void launch(Profile profile, User user) throws Exception {
        Create.createDirs(profile.getVersion());

        JSONObject versionInfo = profile.generateVersionInfo();

        String versionDir = GAME_PATH + "/versions/" + profile.getVersion();
        String jarPath = versionDir + "/" + profile.getVersion() + ".jar";
        String clientURL = versionInfo.getJSONObject("downloads").getJSONObject("client").getString("url");
        if (!Files.exists(Paths.get(jarPath))) {
            Logger.print("Downloading client jar...");
            Download.downloadFile(clientURL, jarPath);
        }

        String classpath = Download.downloadLibs(versionInfo, profile.getVersion());
        classpath += File.pathSeparator + jarPath;

        String assetIndexID = versionInfo.getJSONObject("assetIndex").getString("id");
        Download.downloadAssets(versionInfo);

        Command.buildCommand(profile, user, classpath, versionInfo, assetIndexID);
    }
}
