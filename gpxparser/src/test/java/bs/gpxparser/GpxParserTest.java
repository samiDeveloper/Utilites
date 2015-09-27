package bs.gpxparser;

import javax.xml.stream.XMLStreamException;

import lombok.AllArgsConstructor;

import org.junit.Test;

public class GpxParserTest {

    @AllArgsConstructor
    private static class TestSubscriber implements GpxParseEventSubscriber {
        @Override
        public void handleEvent(GpxParseEvent event) {
            System.out.println(event);
        }
    }

    @Test
    public void testParse() throws XMLStreamException {
        TestSubscriber subscriber = new TestSubscriber();
        GpxParser parser = new GpxParser(subscriber);
        parser.parse(getClass().getResourceAsStream("/schellingw-bijlmer-weesp.gpx"));
    }
}
