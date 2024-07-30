package pl.bnb.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.bnb.entity.OrderProduct;
import pl.bnb.order.BasketItem;
import pl.bnb.services.OrderService;

import java.math.BigDecimal;
import java.util.*;

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
    @GetMapping("/{bookingNumber}")
    public ResponseEntity<List<OrderProduct>> getAllProducts(@PathVariable String bookingNumber) {
        return ResponseEntity.ok(orderService.getAllProduct(bookingNumber));
    }
    @GetMapping("/orderProduct")
    public ResponseEntity<Map<String, Object>> getAllProduct(HttpSession session, Model model) {
        String bookingNumber = basketBN(session,model);
        if (bookingNumber == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<OrderProduct> products = orderService.getAllProduct(bookingNumber);

        BigDecimal totalSum = products.stream()
                .map(OrderProduct::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> response = new HashMap<>();
        response.put("products", products);
        response.put("totalSum", totalSum);

        return ResponseEntity.ok(response);

    }

    @PostMapping("/orderProduct")
    public ResponseEntity<List<OrderProduct>> submitOrder(@RequestBody List<BasketItem> basket, HttpSession session, Model model) {
        String bookingNumber = basketBN(session, model);
        List<OrderProduct> orderProducts = new ArrayList<>();

        for (BasketItem basketItem : basket) {
            Optional<OrderProduct> existingOrder = orderService.findByBookingNumberAndIdDrinkAndIdFood(
                    bookingNumber, basketItem.getIdDrink(), basketItem.getIdFood());

            if (existingOrder.isPresent()) {
                OrderProduct orderProduct = existingOrder.get();
                orderProduct.setQtyProduct(orderProduct.getQtyProduct() + basketItem.getQtyBasketProduct());
                orderProduct.setTotalPrice(orderProduct.getTotalPrice().add(basketItem.getTotalPrice()));
                orderProducts.add(orderProduct);
            } else {
                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setBookingNumber(bookingNumber);
                orderProduct.setIdDrink(basketItem.getIdDrink());
                orderProduct.setIdFood(basketItem.getIdFood());
                orderProduct.setNameProduct(basketItem.getNameBasketProduct());
                orderProduct.setQtyProduct(basketItem.getQtyBasketProduct());
                orderProduct.setTotalPrice(basketItem.getTotalPrice());
                orderProducts.add(orderProduct);
            }
        }

        List<OrderProduct> savedOrders = orderService.saveOrders(orderProducts);
        return ResponseEntity.ok(savedOrders);
    }


//    @PostMapping("/paymentMethod")
//    public String paymentMethod(@RequestBody user)
//    @DeleteMapping("/orderProduct")
//
//    public ResponseEntity<Void> deleteProduct(String bookingNumber, Integer idFood, Integer idDrink){
//        boolean isRemoved = orderService.findByBookingNumberAndIdDrinkAndIdFood(bookingNumber,idFood,idDrink);
//    }


}
