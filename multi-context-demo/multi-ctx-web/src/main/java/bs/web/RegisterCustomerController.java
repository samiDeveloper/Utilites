package bs.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import bs.customer.CustomerApplicationService;

@Controller
public class RegisterCustomerController {

    @Autowired
    private CustomerApplicationService appService;

    @RequestMapping(path = "/registercustomer", method = RequestMethod.GET)
    protected String showRegisterCustomer(HttpServletRequest rq, HttpServletResponse rs)
            throws ServletException, IOException {
        return "customer/register";
    }

    @RequestMapping(path = "/registercustomer", method = RequestMethod.POST)
    protected String doRegisterCustomer(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        long id = appService.registerCustomer("Cust", false);
        System.out.println("***** cust id: "  + id);
        rq.setAttribute("id", id);
        return "customer/registered";
    }
}
