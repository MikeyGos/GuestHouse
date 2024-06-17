package pl.bnb.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.bnb.entity.User;
import pl.bnb.services.PasswordService;
import pl.bnb.services.UserService;

import java.util.Optional;

@Controller
public class PasswordController {

    private final PasswordService passwordService;
    private final UserService userService;

    @Autowired
    public PasswordController(PasswordService passwordService, UserService userService) {
        this.passwordService = passwordService;
        this.userService = userService;
    }

    @PostMapping("/checkPassword")
    public String bothSamePassword(@RequestParam String password, @RequestParam String samePassword, Model model, HttpSession httpSession) {
        System.out.println(password);
        System.out.println(samePassword);

        boolean checkedPassword = passwordService.checkPassword(password, samePassword);
        if (checkedPassword) {
            httpSession.setAttribute("password", password);
            Boolean changed = setPassword((String) httpSession.getAttribute("bookingNumber"), password);

            if (changed) {
                model.addAttribute("message", "Password update correctly!");
                return "redirect:/login.html";      //needed forcing a page change
            }
        }
        model.addAttribute("message", "Booking number or password is invalid!");
        return "register";
    }

    @PatchMapping("/user")
    private Boolean setPassword(String bookingNumber, String password) {
        Optional<User> user = userService.findByBookingNumber(bookingNumber);
        if (user.isPresent()) {
            User u1 = user.get();
            u1.setPassword(password);
            userService.updateUser(u1);
            return true;
        }
        return false;
    }

}
