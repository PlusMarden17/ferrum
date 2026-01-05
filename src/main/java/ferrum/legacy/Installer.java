package ferrum.legacy;

import ferrum.utils.Fetcher;

public class Installer {
    /*
    Щоб грати в старі Beta/Alpha версії потрібні старі версії Java (По типу Java 8)
    для того щоб користувачеві не потрібно було б встановлювати її самому, якщо версія стара то Java 8
    встановиться сама
    */
    public static void start() {
        String urlString = null;
        String os = Fetcher.fetchOS();
        String arch = Fetcher.fetchArch();

        if (arch.contains("x64")) {
            if (os.contains("win")) {
                urlString = "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u392-b08/OpenJDK8U-jdk_x64_windows_hotspot_8u392b08.msi";
            } else if (os.contains("nux")) {
                urlString = "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u392-b08/OpenJDK8U-jdk_x64_linux_hotspot_8u392b08.tar.gz";
            } else if (os.contains("mac")) {
                urlString = "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u392-b08/OpenJDK8U-jdk_x64_mac_hotspot_8u392b08.pkg";
            }
        } else if (arch.contains("x86")) {
            if (os.contains("win")) {
                urlString = "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u392-b08/OpenJDK8U-jdk_x86-32_windows_hotspot_8u392b08.msi";
            } else if (os.contains("nux")) {
                urlString = "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u392-b08/OpenJDK8U-jdk_x86-32_linux_hotspot_8u392b08.tar.gz";
            }
        } else if (arch.contains("arm")) {
            if (os.contains("nux")) {
                urlString = "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u392-b08/OpenJDK8U-jdk_aarch64_linux_hotspot_8u392b08.tar.gz";
            } else if (os.contains("mac")) {
                urlString = "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u392-b08/OpenJDK8U-jdk_aarch64_mac_hotspot_8u392b08.pkg";
            }
        }

        if (urlString != null) {
            String fileName = urlString.substring(urlString.lastIndexOf('/') + 1);
            download(urlString, fileName);
            install(fileName, os);
        } else {
            System.err.println("Error: Couldn't find download link for your OS!");
        }
    }

    public static void download(String urlString, String fileName) {
        try (ReadableByteChannel rbc = Channels.newChannel(new URL(urlString).openStream()); FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            System.out.println("Successfuly downloaded Java 8!");
        } catch (IOException ie) {
            System.err.println("Something went wrong while trying to download Java 8: " + ie.getMessage());
        }
    }

    public static void install(String fileName, String os) {
        try {
            if (os.contains("win")) {
                ProcessBuilder pb = new ProcessBuilder("msiexec", "/i", fileName, "/quiet");
                pb.inheritIO().start().waitFor();
            } else if (os.contains("nux")) {
                ProcessBuilder mk = new ProcessBuilder("mkdir", "-p", "java8_runtime");
                mkdir.start().waitFor();
                ProcessBuilder tar = new ProcessBuilder("tar", "-xzf", fileName, "-C", "java8_runtime", "--strip-components=1");
                tar.inheritIO().start().waitFor();
            } else if (os.contains("mac")) {
                ProcessBuilder pb = new ProcessBuilder("open", fileName);
                pb.start().waitFor();
            }
        } catch (Exception e) {
            System.err.println("An error occurred while trying to install Java 8: " + e.getMessage());
        }
    }

    public static String getJavaPath() {
        String os = Fetcher.fetchOS();
        String path = "java";
        Runtime.Version curr = Runtime.Version();
        int major = curr.feature();

        if (major == 8) {
            if (os.contains("win")) {
                path = "C:\\Program Files\\Eclipse Adoptium\\jdk-8.0.392.8-hotspot\\bin\\java.exe";
            } else if (os.contains("nux")) {
                path = "";
            } else if (os.contains("mac")) {
                path = "";
            }
        } else {
            System.out.println("You don't have Java 8 installed on your machine");
        }

        return path;
    }
}
