package pl.bnb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.bnb.entity.Food;
import pl.bnb.services.FoodService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/food")
public class FoodController {

    private final FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping()
    public ResponseEntity<List<Food>> getAllFood() {
        List<Food> allFood = foodService.getAllFood();
        return ResponseEntity.ok(allFood);
    }

    @GetMapping("/{idFood}")
    public ResponseEntity<Food> getFoodById(@PathVariable(value = "idFood") Integer idFood) {
        Optional<Food> foodById = foodService.getFoodById(idFood);
        return foodById.
                map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping()
    public ResponseEntity<Food> addFood(@RequestBody Food food) {
        Food addedFood = foodService.addFood(food);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{idFood}")
                .buildAndExpand(addedFood.getIdFood())
                .toUri();

        return ResponseEntity.created(location).body(addedFood);
    }

    @PutMapping("/{idFood}")

    public ResponseEntity<Food> updateFood(@PathVariable Integer idFood, @RequestBody Food food) {
        return foodService.getFoodById(idFood)
                .map(updateFood -> {
                    updateFood.setName(food.getName());
                    updateFood.setPrice(food.getPrice());
                    updateFood.setSize(food.getSize());
                    return foodService.addFood(updateFood);
                })
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{idFood}")
    public ResponseEntity<Food> partialUpdateFood(@PathVariable Integer idFood, @RequestBody Food food) {
        return foodService.getFoodById(idFood)
                .map(updateFood -> {
                    if (food.getName() != null) updateFood.setName(food.getName());
                    if (food.getPrice() != null) updateFood.setPrice(food.getPrice());
                    if (food.getSize() != null) updateFood.setSize(food.getSize());
                    return foodService.addFood(updateFood);
                })
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idFood}")
    public ResponseEntity<Void> deleteFood(@PathVariable Integer idFood){
        foodService.delete(idFood);
        return ResponseEntity.noContent().build();
    }

}
