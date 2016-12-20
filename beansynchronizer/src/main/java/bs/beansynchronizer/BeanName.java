package bs.beansynchronizer;

import lombok.Value;

@Value(staticConstructor="of")
class BeanName
{
    private final String beanName;
}
