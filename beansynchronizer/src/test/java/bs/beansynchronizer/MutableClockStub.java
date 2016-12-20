package bs.beansynchronizer;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class MutableClockStub extends Clock {

    private final ZoneId zone;

    private Instant instant;

    /** UTC */
    public MutableClockStub(Instant instant) {
        this(instant, ZoneId.of("UTC"));
    }

    public MutableClockStub(Instant instant,ZoneId zone) {
        super();
        this.instant=instant;
        this.zone = zone;
    }

    @Override
    public ZoneId getZone() {
        return zone;
    }

    /** Returns a new instance */
    @Override
    public Clock withZone(ZoneId zone) {
        return new MutableClockStub(instant, zone);
    }

    @Override
    public Instant instant() {
        return instant;
    }

    public void update(Instant instant) {
        this.instant = instant;
    }
}
