package bs.gpxanalyzer;

import org.junit.Assert;
import org.junit.Test;

public class GeoCalculatorTest {
    @Test
    public void testCalcLatLonRadiansDistenceKm() {
        double lat1 = Math.toRadians(52.38534000d);
        double lon1 = Math.toRadians(4.959440000d);
        double lat2 = Math.toRadians(52.38458000d);
        double lon2 = Math.toRadians(4.961200000d);

        double actualKm = GeoCalculator.calcLatLonRadiansDistenceKm(lat1, lon1, lat2, lon2);

        double deltaCm = 0.00001d;
        Assert.assertEquals(0.14631d, actualKm, deltaCm);
    }

    @Test
    public void testCalcLatLonDegreesDistenceKm() {
        double lat1 = 52.38534000d;
        double lon1 = 4.959440000d;
        double lat2 = 52.38458000d;
        double lon2 = 4.961200000d;

        double actualKm = GeoCalculator.calcLatLonDegreesDistenceKm(lat1, lon1, lat2, lon2);

        double deltaCm = 0.00001d;
        Assert.assertEquals(0.14631d, actualKm, deltaCm);
    }
}
