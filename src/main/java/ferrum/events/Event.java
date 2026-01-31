package ferrum.events;

public class Event {
    private final String message;
    private final double percentage;
    private final long time;

    public Event(String message, double percentage) {
        this.message = message;
        this.percentage = percentage;
        this.time = System.currentTimeMillis();
    }

    public String getMessage() { return message;}
    public double getPercentage() { return percentage; }
    public long getTime() { return time; }
}
