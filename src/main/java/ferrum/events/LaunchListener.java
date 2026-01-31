package ferrum.events;

public interface LaunchListener {
    void onStatusUpdate(Event event);
    void onProgressUpdate(Event event);
    void onError(Event event);
    void onStart(Event event);
}
