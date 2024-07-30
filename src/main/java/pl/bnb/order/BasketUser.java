package pl.bnb.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bnb.entity.OrderProduct;
import pl.bnb.services.OrderService;

import java.util.List;

@RestController()
@RequestMapping("/bnOrder")
public class BasketUser {

    public final OrderService orderService;

    @Autowired
    public BasketUser(OrderService orderService) {
        this.orderService = orderService;
    }


}
