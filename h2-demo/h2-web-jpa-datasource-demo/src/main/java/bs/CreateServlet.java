package bs;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class CreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private long invocations;
    
    public CreateServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        EntityManagerFactory emf = (EntityManagerFactory) ctx.getBean("emf");
        EntityManager em = emf.createEntityManager();

        DemoObject o = new DemoObject();
        o.setTitle("Title " + (invocations + 1));

        em.getTransaction().begin();
        em.persist(o);
        em.getTransaction().commit();
        
        TypedQuery<DemoObject> query = em.createQuery("select d from DemoObject as d", DemoObject.class);
        invocations = query.getResultList().size();

        em.close();

        request.getSession(true).setAttribute("count", invocations);

        response.sendRedirect("/index.jsp");

    }
}
