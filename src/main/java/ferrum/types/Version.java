package ferrum.types;

public enum Version {
    RELEASE("release"),
    SNAPSHOT("snapshot"),
    BETA("old_beta"),
    ALPHA("old_alpha");

    private final String secondName;

    Version(String secondName) {
        this.secondName = secondName;
    }

    public String getSecondName() {
        return secondName;
    }
}
 // якщо зміниться версія джави в них тоді лаунчер не знайле власну ж джаву
 // TODO: пофіксить