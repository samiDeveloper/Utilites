package bs.beansynchronizer;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import lombok.Data;

@Data(staticConstructor="forClient")
final class Lock
{
    public static final int EXPIRY_MINUTES = 2;

    private final UUID clientId;
    private final Instant issuedInstant;

    /** Returns true if not expired according to the specified instant */
    public boolean isValidAt(Instant now)
    {
        if (now.minus(EXPIRY_MINUTES, ChronoUnit.MINUTES).isBefore(issuedInstant))
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
}
