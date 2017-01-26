package bs.beansynchronizer;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import lombok.Data;

@Data(staticConstructor="forClient")
final class Lock
{
    private final UUID clientId;
    private final Instant issuedInstant;
    private final int expiryMins;

    /** Returns true if not expired according to the specified instant */
    public boolean isValidAt(Instant issued)
    {
        if (issued.minus(expiryMins, ChronoUnit.MINUTES).isBefore(issuedInstant))
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

    public Lock renew(Instant issuedInstant)
    {
        return Lock.forClient(clientId, issuedInstant, expiryMins);
    }
}
