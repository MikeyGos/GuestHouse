package pl.bnb.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.bnb.entity.OrderProduct;
import pl.bnb.order.BasketItem;
import pl.bnb.services.OrderService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/login")
    public String basketBN(HttpSession session, Model model) {
        String bookingNumber = (String) session.getAttribute("bookingNumber");
        model.addAttribute("bookingNumber", bookingNumber);
        return bookingNumber;
    }

    @PostMapping("/orderProduct")
    public List<OrderProduct> submitOrder(@RequestBody List<BasketItem> basket, HttpSession session, Model model) {
        List<OrderProduct> orderProducts = new ArrayList<>();
        basket.forEach(item -> {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setBookingNumber(basketBN(session, model));
            orderProduct.setIdDrink(item.getIdDrink());
            orderProduct.setIdFood(item.getIdFood());
            orderProduct.setNameProduct(item.getNameBasketProduct());
            orderProduct.setQtyProduct(item.getQtyBasketProduct());
            orderProduct.setTotalPrice(item.getTotalPrice());
            orderProducts.add(orderProduct);
        });

        return orderService.saveOrders(orderProducts);
    }
}
