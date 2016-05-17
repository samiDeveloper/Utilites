package bs.order;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderApplicationService {
    @PersistenceContext(unitName = "multi-ctx-order")
    private EntityManager em;

    @Transactional("transactionManagerOrder")
    public long placeOrder(long custId, String name) {
        Ordering order = new Ordering(custId, name);
        em.persist(order);
        return order.getId();
    }
}
