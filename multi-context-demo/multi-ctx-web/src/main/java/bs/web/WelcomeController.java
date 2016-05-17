package bs.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WelcomeController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    protected void welcome(HttpServletRequest rq, HttpServletResponse rs)
            throws ServletException, IOException {
        rs.sendRedirect("/registercustomer");
    }
}
