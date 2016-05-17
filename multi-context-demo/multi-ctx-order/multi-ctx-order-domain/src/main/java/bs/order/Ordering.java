package bs.order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
@EqualsAndHashCode
public class Ordering {
    @GeneratedValue
    @Id
    private long id;
    private long custId;
    private String name;

    /** for JPA only */
    protected Ordering() {
        super();
    }

    public Ordering(long custId, String name) {
        super();
        this.custId = custId;
        this.name = name;
    }

}
