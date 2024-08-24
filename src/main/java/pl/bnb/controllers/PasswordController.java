package pl.bnb.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class  PasswordController {

    private final PasswordService passwordService;

    @Autowired
    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("/checkPassword")
    public String bothSamePassword(@RequestParam String password, @RequestParam String samePassword, Model model, HttpSession httpSession) {
        boolean checkedPassword = passwordService.checkPassword(password, samePassword);
        String message = passwordService.getMessage();

        if (checkedPassword) {
            httpSession.setAttribute("password", password);
            boolean changed = passwordService.updatePassword((String) httpSession.getAttribute("bookingNumber"), password);

            if (changed) {
                model.addAttribute("message", message);
                return "redirect:/login.html";
            }
        }
        model.addAttribute("message", message);
        return "register";
    }

    @PatchMapping("/password")
    public ResponseEntity<String> setPassword(@RequestParam String bookingNumber, @RequestParam String password) {
        boolean updated = passwordService.updatePassword(bookingNumber, password);
        if (updated) {
            return ResponseEntity.ok("Password updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

}
