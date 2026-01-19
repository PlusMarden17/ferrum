package ferrum.utils;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Fetcher {
    public static JSONObject fetchJSON(String urlStr) throws IOException, JSONException {
        URL url = new URL(urlStr);
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        return new JSONObject(sb.toString());
    }

    public static String fetchOS() {
        return System.getProperty("os.name").toLowerCase();
    }

    public static String fetchArch() {
        String arch = System.getProperty("os.arch").toLowerCase();

        if (arch.contains("aarch64") || arch.contains("arm")) {
            return "arm";
        } else if (arch.contains("64")) {
            return "x64";
        } else if (arch.contains("i386") ||arch.contains("86")) {
            return "x86";
        } else {
            System.err.println("Error: Unsupported CPU architecture (" + arch + ")");
            return null;
        }
    }

    public static String fetchJDK() {
        return System.getProperty("java.version");
    }
}
