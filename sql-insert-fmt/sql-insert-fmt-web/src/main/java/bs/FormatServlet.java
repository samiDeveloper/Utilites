package bs;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/format")
public class FormatServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ServletConfig config;

    @Override
    public void init(javax.servlet.ServletConfig config) throws ServletException {
        this.config = config;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        request.setAttribute("sql", request.getParameter("sql"));
        request.setAttribute("indent", "TODO");
        request.setAttribute("spacing", "TODO");
        request.setAttribute("width", "TODO");
        request.setAttribute("formatted", "TODO");
        config.getServletContext().getRequestDispatcher("/format.jsp").forward(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        config.getServletContext().getRequestDispatcher("/format.jsp").forward(request, response);
    }
}
