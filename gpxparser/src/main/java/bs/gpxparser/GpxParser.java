package bs.gpxparser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public final class GpxParser {
    private final GpxParseEventSubscriber subscriber;
    
    public GpxParser(GpxParseEventSubscriber subscriber) {
        super();
        this.subscriber = subscriber;
    }
    
    public void parse(InputStream is) throws XMLStreamException {
        XMLInputFactory f = XMLInputFactory.newInstance();
        XMLStreamReader r = f.createXMLStreamReader(is);

        toElement("gpx", r);
        r.nextTag();
        parseTracks(r);
    }

    private void parseTracks(XMLStreamReader r) throws XMLStreamException {
        while(findNextSibbling(r, "trk")) {
            parseTrack(r);
        }
    }

    private void parseTrack(XMLStreamReader r) throws XMLStreamException {
        Optional<String> trackName = fetchNextElementContent("name", r);
        subscriber.handleEvent(new StartTrackEvent(trackName.orElse("<TZN>")));
        
        parseTrackSegments(r);
        subscriber.handleEvent(new EndTrackEvent());
    }

    private void parseTrackSegments(XMLStreamReader r) throws XMLStreamException {
        while(findNextSibbling(r, "trkseg")) {
            subscriber.handleEvent(new StartTrackSegmentEvent());
            r.next();
            
            parseTrackPoints(r);
            
            toEndElement(r, "trkseg");
            subscriber.handleEvent(new EndTrackSegmentEvent());
            r.next();
        }
    }

    private void parseTrackPoints(XMLStreamReader r) throws XMLStreamException {
        while(findNextSibbling(r, "trkpt")) {
            Map<String, String> atts=parseAttributes( r);
            Optional<String> ele = fetchNextElementContent("ele", r);
            Optional<String> time = fetchNextElementContent("time", r);
            
            subscriber.handleEvent(new TrackPointEvent(atts.get("lat"), atts.get("lon"),  ele.orElse(""), time.orElse("")));

            toEndElement(r, "trkpt");
            r.next();
        }
    }

    private static boolean findNextSibbling(XMLStreamReader r, String elementName) throws XMLStreamException {
        while (r.hasNext()) {
            if (r.isStartElement()) {
                if (r.getLocalName().equals(elementName)) {

                    return true;
                } else {

                    toEndElement(r ,r.getLocalName());
                }
            } else if (r.isEndElement()) {

                return false;
            }
            r.next();
        }
        return false;
    }

    /** precon: r is at ptType typed element */
    private static Map<String, String> parseAttributes(XMLStreamReader r) {
        Map<String, String> atts =      new HashMap<>();
        atts.put(r.getAttributeLocalName(0), r.getAttributeValue(0));
        atts.put(r.getAttributeLocalName(1), r.getAttributeValue(1));
        return atts;
    }

    private static boolean toElement(String elementName, XMLStreamReader r) throws XMLStreamException {
        while (r.hasNext()) {
            if (r.isStartElement() && r.getLocalName().equals(elementName)) {

                return true;
            }
            r.next();
        }
        return false;
    }

    private static Optional<String> fetchNextElementContent(String elementName, XMLStreamReader r) throws XMLStreamException {
        if(r.isStartElement() && !r.getLocalName().equals(elementName)) {
            r.nextTag();
        }
        
        while (r.hasNext()) {
            if (r.isStartElement()) {
                if (r.getLocalName().equals(elementName)) {

                    Optional<String> result = Optional.of(r.getElementText());
                    toEndElement(r, elementName);
                    r.next();
                    return result;
                } else {

                    return Optional.empty();
                }
            } else if (r.isEndElement()) {

                return Optional.empty();
            }
            r.next();
        }
        return Optional.empty();
    }

    private static void toEndElement(XMLStreamReader r, String localName) throws XMLStreamException {
        while (r.hasNext()) {
            if (r.isEndElement() && r.getLocalName().equals(localName)) {
                return;
            }
            r.next();
        }
    }

}
