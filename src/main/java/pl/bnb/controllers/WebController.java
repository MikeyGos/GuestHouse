package pl.bnb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("index.html")
    public String index() {
        return "index";
    }

    @GetMapping("about.html")
    public String about() {
        return "about";
    }

    @GetMapping("reservation.html")
    public String reservation() {
        return "reservation";
    }

    @GetMapping("products.html")
    public String products() {
        return "products";
    }

    @GetMapping("store.html")
    public String store() {
        return "store";
    }

    @GetMapping("login.html")
    public String login() {
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

    @GetMapping("order.html")
    public String order() {
        return "order";
    }

    @GetMapping("basket.html")
    public String userOrder() {
        return "basket";
    }

    @GetMapping("snacks.html")
    public String snacks() {
        return "snacks";
    }

    @GetMapping("userbasket.html")
    public String userBasket() {
        return "userbasket";
    }

    @GetMapping("payment.html")
    public String payment() {
        return "payment";
    }

    @GetMapping("room.html")
    public String partyRoom() {
        return "room";
    }

    @GetMapping("roomList.html")
    public String roomList() {
        return "roomList";
    }

    @GetMapping("admin.html")
    public String admin() {
        return "admin";
    }

    @GetMapping("searchRoom.html")
    public String searchRoom() {
        return "searchRoom";
    }

    @GetMapping("searchOrder.html")
    public String searchOrder() {
        return "searchOrder";
    }
    @GetMapping("addFood.html")
    public String addFood() {
        return "addFood";
    }

    @GetMapping("addDrink.html")
    public String addDrink() {
        return "addDrink";
    }

    @GetMapping("fullBookingList.html")
    public String searchFullOrder() {
        return "fullBookingList";
    }

}

