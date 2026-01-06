package ferrum.types;

// У розробці
public enum JDKPackage {
    WIN_X64("win", "x64", "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u392-b08/OpenJDK8U-jdk_x64_windows_hotspot_8u392b08.msi"),
    WIN_X86("win", "x86", "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u392-b08/OpenJDK8U-jdk_x86-32_windows_hotspot_8u392b08.msi"),

    LIN_X64("nux", "x64", ""),
    LIN_X86("nux", "x86", ""),
    LIN_ARM("nux", "arm", ""),

    MAC_X64("mac", "x64", ""),
    MAC_ARM("mac", "arm", "");

    JDKPackage(String os, String arch, String urlString) {

    }
}
