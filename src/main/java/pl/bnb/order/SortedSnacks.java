package pl.bnb.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bnb.entity.Food;
import pl.bnb.services.FoodService;

import java.util.List;

@RestController
public class SortedSnacks {
    public final FoodService foodService;

@Autowired
    public SortedSnacks(FoodService foodService) {
        this.foodService = foodService;
    }
    @GetMapping("/sortedSnacks")
    public List<Food> getSortedSnacks() {
    return foodService.getAllFood();
    }
}
