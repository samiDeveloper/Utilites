package bs.gpxparser;

public interface GpxParseEventSubscriber {
    void handleEvent(GpxParseEvent event);
}
