package ferrum.legacy;

import ferrum.utils.Fetcher;
import ferrum.utils.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Installer {
    public static void start() {
        String urlString = null;
        String os = Fetcher.fetchOS();
        String arch = Fetcher.fetchArch();

        if (arch == null) {
            Logger.error("Error: Unknown system architecture!");
            return;
        }

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
            if (download(urlString, fileName)) {
                install(fileName, os);
            }
        } else {
            Logger.error("Error: Couldn't find download link for your OS!");
        }
    }

    public static boolean download(String urlString, String fileName) {
        try (ReadableByteChannel rbc = Channels.newChannel(new URL(urlString).openStream()); FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            Logger.print("Successfuly downloaded Java 8!");
        } catch (IOException ie) {
            Logger.error("Something went wrong while trying to download Java 8: " + ie.getMessage());
        }
    }

    public static void install(String fileName, String os) {
        try {
            if (os.contains("win")) {
                ProcessBuilder pb = new ProcessBuilder("msiexec", "/i", fileName, "/quiet");
                pb.inheritIO().start().waitFor();
            } else if (os.contains("nux")) {
                ProcessBuilder mk = new ProcessBuilder("mkdir", "-p", "java8_runtime");
                mk.start().waitFor();
                ProcessBuilder tar = new ProcessBuilder("tar", "-xzf", fileName, "-C", "java8_runtime", "--strip-components=1");
                tar.inheritIO().start().waitFor();
            } else if (os.contains("mac")) {
                ProcessBuilder pb = new ProcessBuilder("open", fileName);
                pb.start().waitFor();
            }
        } catch (Exception e) {
            Logger.error("An error occurred while trying to install Java 8: " + e.getMessage());
        }
    }

    public static String getJavaPath() {
        String os = Fetcher.fetchOS();
        String jdk = Fetcher.fetchJDK();

        if (jdk.startsWith("1.8")) {
            return "java";
        }

        String locPath = "";

        if (os.contains("win")) {
            File ij = new File("C:\\Program Files\\Eclipse Adoptium\\jdk-8.0.392.8-hotspot\\bin\\java.exe");
            if (ij.exists()) {
                locPath = ij.getAbsolutePath();
            } else {
                Logger.error("Error: Unable to find Java 8 on your machine!");
            }
        } else if (os.contains("nux")) {
            locPath = new File("java8_runtime/bin/java").getAbsolutePath();
        } else if (os.contains("mac")) {
            locPath = new File("java8_runtime/Contents/Home/bin/java").getAbsolutePath();
        } else {
            Logger.error("Error: Unsupported OS!");
        }

        File jFile = new File(locPath);
        if (jFile.exists()) {
            return locPath;
        }

        Logger.error("Error: Unable to find Java 8 on your machine!");
        return null;
    }
}
