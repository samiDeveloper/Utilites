package bs.beansynchronizer;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import lombok.Value;

@Value(staticConstructor = "forClient")
final class Lock
{
    UUID clientId;
    Instant issuedInstant;
    int expirySecs;

    /** Returns true if not expired according to the specified instant */
    public boolean isValidAt(Instant target)
    {
        if (target.isBefore(issuedInstant.plus(expirySecs, ChronoUnit.SECONDS)))
        {
            return true;
        } else
        {
            return false;
        }
    }

    public boolean isOwnedBy(UUID clientId)
    {
        return clientId.equals(this.clientId);
    }

    public Lock renew(Instant newIssuedInstant)
    {
        Lock renewed = Lock.forClient(clientId, newIssuedInstant, expirySecs);
        return renewed;
    }
}
