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
    public String login(@RequestParam("bookingNumber") String bookingNumber, @RequestParam("password") String password,
            Model model, HttpSession session) {
        if (password.isEmpty()) {
            return handleRegistration(bookingNumber, model, session);
        }
        return handleLogin(bookingNumber, password, model, session);
    }

    private String handleLogin(String bookingNumber, String password, Model model, HttpSession session) {
        boolean isLogged = loginService.validateUser(bookingNumber, password);

        if (isLogged) {
            session.setAttribute("bookingNumber", bookingNumber);

            if (loginService.isUserAdmin(bookingNumber)) {
                return "redirect:/admin.html";
            } else {
                return "redirect:/index.html";
            }
        } else {
            model.addAttribute("message", "Booking number or password is invalid!");
            return "login";
        }
    }

    private String handleRegistration(String bookingNumber, Model model, HttpSession session) {
        boolean isRegistrationValid = loginService.validateUserToRegister(bookingNumber);

        if (isRegistrationValid) {
            session.setAttribute("bookingNumber", bookingNumber);
            model.addAttribute("message", "Your reservation number is correct, set a password for your account.");
            return "register";
        } else {
            model.addAttribute("message", "Booking number or password is invalid!");
            return "login";
        }
    }
}
