package io.coconut.ferrum.files;

import io.coconut.ferrum.Launcher;
import io.coconut.ferrum.utils.Fetcher;
import org.json.*;

import java.io.IOException;

public class Download {

    public static void downloadVersion(String version) {
        // В розробці...
    }

    public static String findURL(String version) {
        try {
            JSONObject manifest = Fetcher.fetchJSON(Launcher.VERSION_LIST);
            JSONArray versions = manifest.getJSONArray("versions");
            for (int i = 0; i < versions.length(); i++) {
                JSONObject versionTemp = versions.getJSONObject(i);
                if (versionTemp.getString("id").equals(version)) {
                    return versionTemp.getString("url");
                }
            }

            return null;
        } catch (IOException ie) {
            System.out.println("An error occurred while trying to read version manifest: " + ie.getMessage());
            return null;
        } catch (JSONException je) {
            System.out.println("An error occurred while trying to parse JSON: " + je.getMessage());
            return null;
        }
    }
}
