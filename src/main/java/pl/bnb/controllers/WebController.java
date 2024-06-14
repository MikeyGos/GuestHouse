package pl.bnb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("index.html")
    public String index(){
        return "index";
    }

    @GetMapping("about.html")
    public String about(){
        return "about";
    }

    @GetMapping("reservation.html")
    public String reservation(){
        return "reservation";
    }

    @GetMapping("products.html")
    public String products(){
        return "products";
    }

    @GetMapping("store.html")
    public String store() {
        return "store";
    }
    @GetMapping("login.html")
    public String login(){
        return "login";
    }

    @GetMapping("food.html")
    public String food() {
        return "food";
    }

    @GetMapping("drinks.html")
    public String drinks() {
        return "drinks";
    }

    @GetMapping("register.html")
    public String register() {
        return "register";
    }
}

