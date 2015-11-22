package bs.gpxanalyzer;

import java.time.Duration;

import rx.Observer;
import bs.gpxparser.TrackPointEvent;

public class CalculateSpeedObserver implements Observer<TrackPointEvent> {

    private TrackPointEvent prev = null;

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        System.out.println(e);
    }

    @Override
    public void onNext(TrackPointEvent t) {
        if (prev == null) {
            prev = t;
            return;
        }
        
        TrackPoint prevTp = TrackPoint.fromTrackPointEvent(prev);
        TrackPoint tp = TrackPoint.fromTrackPointEvent(t);
        
        double kmh = GeoCalculator.speedKmhFor(prevTp, tp);
        double ms = GeoCalculator.speedMsFor(prevTp, tp);
        Duration duration = GeoCalculator.duration(prevTp, tp);

        System.out.println(duration.getSeconds() + "."+duration.getNano()/1000+" sec " + kmh+" kmh, " + ms + " ms");

        prev = t;
    }

}
