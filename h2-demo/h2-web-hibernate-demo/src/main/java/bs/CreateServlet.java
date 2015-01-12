package bs;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.h2.tools.Server;
import org.hibernate.Session;

public class CreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private int invocations;
    
    {
        try {
            
            Server.createTcpServer("-tcpDaemon", "-tcpPort", "9092").start();

            // Now connect using jdbc 'jdbc:h2:tcp://localhost:9092/mem:demodb;IFEXISTS=TRUE'
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CreateServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        DemoObject o = new DemoObject(++invocations);
        o.setTitle("Title " + invocations);
        session.save(o);

        session.getTransaction().commit();
        
        request.getSession(true).setAttribute("count", invocations);
        
        response.sendRedirect("/index.jsp");
    }
}
