package pl.bnb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bnb.entity.Food;
import pl.bnb.repositories.FoodRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {
    private final FoodRepository foodRepository;

    @Autowired
    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public List<Food> getAllFood() {
        return foodRepository.findAll();
    }

    public Optional<Food> getFoodById(Integer idFood) {
        return foodRepository.findById(idFood);
    }

    public Optional<Food> getFoodByName(String name) {
        return foodRepository.findByName(name);
    }
    public Food addFood(Food food) {
        return foodRepository.save(food);
    }

    public void delete(Integer idFood) {
        foodRepository.deleteById(idFood);
    }

}


