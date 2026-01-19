package ferrum.core;

import ferrum.files.Download;
import ferrum.types.Version;
import org.json.JSONObject;

public class Profile {
    /*
    Profile - Це одиниця версії в лаунчері. Вона має саму версію та її налаштування.
    Тип версії самовизначається при виборі версії через setVersion();
     */
    private String version;
    private int minMem;
    private int maxMem;
    private Version versionType;

    public void decideType() {
        if (version.startsWith("a") || version.startsWith("c") || version.startsWith("rd") || version.startsWith("inf")) {
            this.versionType = Version.ALPHA;
        } else if (version.startsWith("b")) {
            this.versionType = Version.BETA;
        } else if (version.contains("w") || version.contains("snapshot") || version.contains("pre") || version.contains("rc")) {
            this.versionType = Version.SNAPSHOT;
        } else {
            this.versionType = Version.RELEASE;
        }
    }

    public JSONObject generateVersionInfo() throws Exception {
        String versionURL = Download.findURL(this.version);
        if (versionURL == null) {
            throw new RuntimeException("Version not found!");
        }

        JSONObject versionInfo = Download.downloadJSON(versionURL);
        return versionInfo;
    }

    public void setVersion(String v) {
        this.version = v;
        decideType();
    }

    public void setMemory(int min, int max) {
        this.minMem = min;
        this.maxMem = max;
    }

    public void setMinMem(int min) { this.minMem = min; }
    public void setMaxMem(int max) { this.maxMem = max; }

    public String getVersion() { return version; }
    public int getMinMem() { return minMem; }
    public int getMaxMem() { return maxMem; }
    public Version getVersionType() { return versionType; }
}