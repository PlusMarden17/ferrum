package ferrum.events;

public interface FilesListener {
    void onCreateDirectories(Event event);
    void onNativesExtract(Event event);
    void onFileDownload(Event event);
    void onLibDownload(Event event);
    void onAssetDownload(Event event);
}
