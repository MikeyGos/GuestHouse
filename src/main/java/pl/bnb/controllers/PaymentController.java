package pl.bnb.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.bnb.entity.User;
import pl.bnb.services.OrderService;
import pl.bnb.services.UserService;

import java.util.Optional;

@RestController
public class PaymentController {

    private final UserService userService;
private final OrderController orderController;
    @Autowired
    public PaymentController(UserService userService, OrderController orderController) {
        this.userService = userService;
        this.orderController = orderController;
    }


    @PostMapping("/paymentMethod")
    public ResponseEntity<String> paymentMethod(@RequestBody User userPayment, HttpSession session, Model model) {
        String bn = orderController.basketBN(session, model);

        if (bn !=null && !bn.isEmpty()) {
            Optional<User> optionalUser = userService.findByBookingNumber(bn);
           if(optionalUser.isPresent()) {
               User user = optionalUser.get();
               user.setPayOnline(userPayment.getPayOnline());
               userService.paymentUser(user);
           }
            return ResponseEntity.ok("Payment method updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
