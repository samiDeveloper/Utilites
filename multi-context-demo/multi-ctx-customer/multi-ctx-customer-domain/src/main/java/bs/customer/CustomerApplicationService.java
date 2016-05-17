package bs.customer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CustomerApplicationService {
    @PersistenceContext(unitName = "multi-ctx-customer")
    private EntityManager em;

    @Transactional("transactionManagerCustomer")
    public long registerCustomer(String name, boolean premium) {
        Customer cust = new Customer(name, premium);
        em.persist(cust);
        return cust.getId();
    }
}
