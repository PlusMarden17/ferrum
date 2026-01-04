package io.coconut.ferrum;

public enum VersionType {
    RELEASE("release"),
    SNAPSHOT("snapshot"),
    BETA("old_beta"),
    ALPHA("old_alpha");

    private final String friendlyName;

    VersionType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}
