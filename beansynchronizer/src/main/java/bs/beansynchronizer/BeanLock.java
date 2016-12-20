package bs.beansynchronizer;

import java.util.UUID;

import lombok.Data;

/** Controls access to a spring bean identified by it's name */
@Data
class BeanLock
{
    private final BeanName beanName;
    private final UUID uuid;
}
