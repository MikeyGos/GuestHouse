package pl.bnb.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.bnb.services.LoginService;

@Controller
public class LoginController {
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;

    }

    @PostMapping("/login.html")
    public String login(@RequestParam("bookingNumber") String bookingNumber, @RequestParam("password") String password, Model model, HttpSession httpSession) {
        if (!password.isEmpty()) {
            boolean isLogged = loginService.validateUser(bookingNumber, password);
            if (isLogged) {
                httpSession.setAttribute("bookingNumber", bookingNumber);
                return "redirect:/index.html"; // must be forced to index

            } else {
                model.addAttribute("message", "Booking number or password is invalid!");
                return "login";
            }
        }
        boolean validateUserForRegister = loginService.validateUserToRegister(bookingNumber);
        if (validateUserForRegister) {
            httpSession.setAttribute("bookingNumber", bookingNumber);
            model.addAttribute("message", "Your reservation number is correct, set a password for your account.");
            return "register";
        } else {
            model.addAttribute("message", "Booking number or password is invalid!");
            return "login";
        }
    }
}
