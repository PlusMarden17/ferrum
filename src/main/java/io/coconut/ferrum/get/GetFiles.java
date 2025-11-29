package io.coconut.ferrum.get;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.json.*;

import static io.coconut.ferrum.Launcher.MINECRAFT_DIR;

public class GetFiles {
    private static final String VERSIONS_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json";

    private static void createDirectories() throws IOException {
        Files.createDirectories(Paths.get(MINECRAFT_DIR + "/versions"));
        Files.createDirectories(Paths.get(MINECRAFT_DIR + "/libraries"));
        Files.createDirectories(Paths.get(MINECRAFT_DIR + "/assets/indexes"));
        Files.createDirectories(Paths.get(MINECRAFT_DIR + "/assets/objects"));
        Files.createDirectories(Paths.get(MINECRAFT_DIR + "/natives"));
    }

    private static JSONObject downloadJSON(String urlString) throws Exception {
        URL url = new URL(urlString);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(url.openStream()));
        StringBuilder json = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            json.append(line);
        }
        reader.close();

        return new JSONObject(json.toString());
    }

    private static void downloadFile(String urlString, String destination)
            throws Exception {
        Files.createDirectories(Paths.get(destination).getParent());

        URL url = new URL(urlString);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(destination);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }

    private static boolean checkRules(JSONArray rules) {
        String os = System.getProperty("os.name").toLowerCase();

        for (int i = 0; i < rules.length(); i++) {
            JSONObject rule = rules.getJSONObject(i);
            String action = rule.getString("action");

            if (rule.has("os")) {
                JSONObject osRule = rule.getJSONObject("os");
                String osName = osRule.optString("name", "");

                boolean matches = false;
                if (osName.equals("windows") && os.contains("win")) matches = true;
                if (osName.equals("linux") && os.contains("nux")) matches = true;
                if (osName.equals("osx") && os.contains("mac")) matches = true;

                if (action.equals("allow") && !matches) return false;
                if (action.equals("disallow") && matches) return false;
            }
        }

        return true;
    }

    private static String getNativeKey() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            return "natives-windows";
        } else if (os.contains("nux")) {
            return "natives-linux";
        } else if (os.contains("mac")) {
            return "natives-macos";
        } else if (os.contains("bsd")) {
            return "native-bsd";
        }

        return null;
    }

    private static void extractNatives(String jarPath, String destDir) throws Exception {
        Files.createDirectories(Paths.get(destDir));

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(jarPath))) {
            ZipEntry entry;
            byte[] buffer = new byte[8192];

            while ((entry = zis.getNextEntry()) != null) {
                String name = entry.getName();

                if (name.startsWith("META-INF/") || name.contains(".git") ||
                        name.contains(".sha1")) {
                    continue;
                }

                if (name.endsWith(".dll") || name.endsWith(".so") ||
                        name.endsWith(".dylib") || name.endsWith(".jnilib")) {

                    File outFile = new File(destDir, new File(name).getName());

                    try (FileOutputStream fos = new FileOutputStream(outFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }

                zis.closeEntry();
            }
        }
    }
}
