package bs.gpxparser;

import lombok.Data;

@Data
public class StartTrackEvent extends GpxParseEvent {

    private final String trackName;

}
