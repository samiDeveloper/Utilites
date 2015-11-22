package bs.gpxanalyzer;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.xml.stream.XMLStreamException;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import bs.gpxparser.BlockingSubscriber;
import bs.gpxparser.GpxParseEvent;
import bs.gpxparser.GpxParser;
import bs.gpxparser.TrackPointEvent;

public class GpxAnalyzerDemo {
    public static void main(String[] args) throws XMLStreamException, InterruptedException {
        // demoBlockingSubscriberAsync();

        demoCalcSpeed();
    }

    private static void demoCalcSpeed() throws XMLStreamException, InterruptedException {
        BlockingSubscriber blockingSubscriber = new BlockingSubscriber(1);
        GpxParser gpxParser = new GpxParser(blockingSubscriber);
        Observable<GpxParseEvent> gpxObservable = Observable.from(blockingSubscriber);


//        InputStream is = GpxAnalyzerDemo.class.getResourceAsStream("/schellingw-bijlmer-weesp.gpx");
        InputStream is = GpxAnalyzerDemo.class.getResourceAsStream("/Track_04-OCT-15 122442 PM.gpx");
        
        gpxParser.parse(is);

        Observer<? super TrackPointEvent> o=new CalculateSpeedObserver();
        gpxObservable.filter(e -> e instanceof TrackPointEvent).map(e -> (TrackPointEvent) e).subscribe(o);
    }

    private static void demoBlockingSubscriberAsync() throws XMLStreamException, InterruptedException {
        BlockingSubscriber blockingSubscriber = new BlockingSubscriber(1);
        GpxParser gpxParser = new GpxParser(blockingSubscriber);
        Observable<GpxParseEvent> gpxObservable = Observable.from(blockingSubscriber);

        CountDownLatch finSignal = new CountDownLatch(1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                gpxObservable.subscribe(System.out::println);
                finSignal.countDown();
            }
        }).start();

        InputStream is = GpxAnalyzerDemo.class.getResourceAsStream("/schellingw-bijlmer-weesp.gpx");
        gpxParser.parse(is);

        finSignal.await(1, TimeUnit.SECONDS);
    }

}
