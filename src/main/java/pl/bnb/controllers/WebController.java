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

    @GetMapping("login.html")
    public String login(){
        return "login";
    }
}
