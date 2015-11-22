package bs.gpxanalyzer;

import java.time.Duration;

/**
 * <a href="http://andrew.hedges.name/experiments/haversine/">http://andrew.hedges.name/experiments/haversine/</a> 
 */
public class GeoCalculator {
    private final static double EARTH_RADIUS_KM = 6371d;

    /**
     * Returns the distance in km between the specified GPX trackPoints
     */
    public static double calcTrackPointsDistenceKm(TrackPoint t1, TrackPoint t2) {
        return calcLatLonDegreesDistenceKm(t1.getLat(), t1.getLon(), t2.getLat(), t2.getLon());
    }

    /**
     * Returns the distance in km between the specified WGS84 earth model (GPX) coordinates in
     * <strong>degrees</strong>.
     * <p>
     * For example 52.38534, 4.95944, 52.38458, 4.9612 returns 0.146 km (rounded)
     */
    public static double calcLatLonDegreesDistenceKm(double latDeg1, double lonDeg1, double latDeg2, double lonDeg2) {
        double latRad1 = Math.toRadians(latDeg1);
        double lonRad1 = Math.toRadians(lonDeg1);
        double latRad2 = Math.toRadians(latDeg2);
        double lonRad2 = Math.toRadians(lonDeg2);

        return calcLatLonRadiansDistenceKm(latRad1, lonRad1, latRad2, lonRad2);
    }

    /**
     * Returns the distance in km between the specified WGS84 earth model (GPX) coordinates in
     * <strong>radians</strong>.
     * <p>
     * For example 0.9142966627766863, 0.08655855705510757, 0.9142833982743711, 0.08658927484994267 returns
     * 0.146 km (rounded)
     */
    public static double calcLatLonRadiansDistenceKm(double latRad1, double lonRad1, double latRad2, double lonRad2) {
        double dlat = latRad2 - latRad1;
        double dlon = lonRad2 - lonRad1;

        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(latRad1) * Math.cos(latRad2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = EARTH_RADIUS_KM * c;

        return d;
    }

    public static double speedKmhFor(TrackPoint t1, TrackPoint t2) {
        Duration duration = duration(t1, t2);
        double hours = duration.toMillis() / (1000d * 3600l);
        
        double distanceKm = calcTrackPointsDistenceKm(t1, t2);
        double kmh = distanceKm / hours;
        return kmh;
    }

    public static double speedMsFor(TrackPoint t1, TrackPoint t2) {
        Duration duration = duration(t1, t2);
        double sec = duration.toMillis() / 1000d;
        
        double distanceM = calcTrackPointsDistenceKm(t1, t2) * 1000;
        double ms = distanceM / sec;
        return ms;
    }

    public static Duration duration(TrackPoint t1, TrackPoint t2) {
        Duration duration = Duration.between(t1.getDateTime(), t2.getDateTime());
        return duration;
    }
}
