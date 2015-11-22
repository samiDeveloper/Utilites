package bs.gpxanalyzer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import bs.gpxparser.TrackPointEvent;

@EqualsAndHashCode
@ToString
@Getter
public class TrackPoint {
    private final double lat, lon;
    private final LocalDateTime dateTime;

    public static TrackPoint fromTrackPointEvent(TrackPointEvent e) {
        double lat = Double.parseDouble(e.getLat());
        double lon = Double.parseDouble(e.getLon());
        LocalDateTime dateTime = LocalDateTime.parse(e.getTime(), DateTimeFormatter.ISO_DATE_TIME);

        return new TrackPoint(lat, lon, dateTime);
    }

    private TrackPoint(double lat, double lon, LocalDateTime dateTime) {
        super();
        this.lat = lat;
        this.lon = lon;
        this.dateTime = dateTime;
    }
}
