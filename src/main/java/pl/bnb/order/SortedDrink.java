package pl.bnb.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bnb.entity.Drink;
import pl.bnb.services.DrinkService;

import java.util.List;

@RestController()
public class SortedDrink {

    public final DrinkService drinkService;

    @Autowired
    public SortedDrink(DrinkService drinkService) {
        this.drinkService = drinkService;
    }


    @GetMapping("/sortedDrinks")
    public List<Drink> sortedDrink() {
        return drinkService.getAllDrink();
    }
}
