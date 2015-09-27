package bs.gpxparser;

import lombok.Data;

@Data
public class TrackPointEvent extends GpxParseEvent {
    final String lat, lon, ele, time;
}
