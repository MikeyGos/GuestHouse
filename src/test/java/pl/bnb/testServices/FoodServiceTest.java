package pl.bnb.testServices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bnb.entity.Food;
import pl.bnb.repositories.FoodRepository;
import pl.bnb.services.FoodService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FoodServiceTest {

    @Mock
    private FoodRepository foodRepository;

    @InjectMocks
    private FoodService foodService;


    @Test
    public void testGetAllFood() {
        Food food = new Food();
        food.setName("Bar");
        Food food2 = new Food();
        food2.setName("Crispy");
        List<Food> foodList = Arrays.asList(food, food2);

        when(foodRepository.findAll()).thenReturn(foodList); // Mockito.when == when -> Mockito added static import
        List<Food> result = foodService.getAllFood();

        assertEquals(2, result.size());
        assertEquals("Bar", result.get(0).getName());
        assertEquals("Crispy", result.get(1).getName());
    }

    @Test
    public void testGetFoodByID() {
        Food food = new Food();
        food.setIdFood(1);
        food.setName("Bar");

        when(foodRepository.findById(1)).thenReturn(Optional.of(food));
        Optional<Food> result = foodService.getFoodById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getIdFood());
        assertEquals("Bar", result.get().getName());
    }

    @Test
    public void testGetFoodByName() {
        Food food = new Food();
        food.setIdFood(1);
        food.setName("Bar");

        when(foodRepository.findByName(food.getName())).thenReturn(Optional.of(food));
        Optional<Food> result = foodService.getFoodByName("Bar");

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getIdFood());
        assertEquals("Bar", result.get().getName());
    }

    @Test
    public void TestAddFood() {
        Food food = new Food();
        food.setIdFood(1);
        food.setName("Bar");
        food.setPrice(BigDecimal.valueOf(2.5));
        food.setSize("Pack");
        food.setQtyFood(1);

        when(foodRepository.save(food)).thenReturn(food);
        Food addedFood = foodService.addFood(food);

        assertEquals(1, addedFood.getIdFood());
        assertEquals("Bar", addedFood.getName());
        assertEquals(BigDecimal.valueOf(2.5), addedFood.getPrice());
        assertEquals("Pack", addedFood.getSize());
        assertEquals(1, addedFood.getQtyFood());

        verify(foodRepository, times(1)).save(food);
    }

    @Test
    public void TestDeleteFood() {
        foodService.delete(1);
        verify(foodRepository, times(1)).deleteById(1);
    }
}
