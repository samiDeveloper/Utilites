package bs.gpxparser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Test;

public class GpxParserTest {

    private static class TestSubscriber implements GpxParseEventSubscriber {
        public final List<GpxParseEvent> recorded = new ArrayList<>();

        @Override
        public void handleEvent(GpxParseEvent event) {
            recorded.add(event);
        }
    }

    @Test
    public void testParseSmoke() throws XMLStreamException {
        TestSubscriber subscriber = new TestSubscriber();
        GpxParser parser = new GpxParser(subscriber);

        parser.parse(getClass().getResourceAsStream("/gpxParserTest.gpx"));

        Assert.assertEquals(20, subscriber.recorded.size());
        Assert.assertEquals(2, subscriber.recorded.stream().filter(e -> e instanceof StartTrackEvent).count());

        TrackPointEvent expected = new TrackPointEvent("52.38534000", "4.959440000", "1.0000000", "2010-01-01T00:00:00Z");
        Assert.assertEquals(expected, subscriber.recorded.stream().filter(e -> e instanceof TrackPointEvent).findFirst().get());
    }
}
