package bs.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import bs.order.OrderApplicationService;

@Controller
public class PlaceOrderController {

    @Autowired
    private OrderApplicationService appService;

    @RequestMapping(path = "/placeorder", method = RequestMethod.GET)
    protected String showPlaceOrder(HttpServletRequest rq, HttpServletResponse rs)
            throws ServletException, IOException {
        return "order/place";
    }

    @RequestMapping(path = "/placeorder", method = RequestMethod.POST)
    protected String doCreateOrder(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        long id = appService.placeOrder(1, "Order");
        System.out.println("***** order id: "  + id);
        rq.setAttribute("id", id);
        return "order/placed";
    }
}
