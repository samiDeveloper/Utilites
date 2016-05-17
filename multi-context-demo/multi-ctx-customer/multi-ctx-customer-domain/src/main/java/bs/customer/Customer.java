package bs.customer;

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
public class Customer {
    @GeneratedValue
    @Id
    private long id;
    private String name;
    private boolean premium;

    /** for JPA only */
    protected Customer() {
        super();
    }

    public Customer(String name, boolean premium) {
        super();
        this.name = name;
        this.premium = premium;
    }

}
