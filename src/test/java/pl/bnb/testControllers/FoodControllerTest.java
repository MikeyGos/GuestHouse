package pl.bnb.testControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.bnb.controllers.FoodController;
import pl.bnb.entity.Food;
import pl.bnb.services.FoodService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FoodController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FoodControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private FoodService foodService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllFood() throws Exception {
        Food food = new Food();
        food.setIdFood(1);
        food.setName("bar");
        Food food2 = new Food();
        food2.setIdFood(2);
        food2.setName("baz");

        when(foodService.getAllFood()).thenReturn(List.of(food, food2));

        mvc.perform(get("/food")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idFood").value(1))        //if[0] - expect food - first on list, if [1] food2
                .andExpect(jsonPath("$[0].name").value("bar"))
                .andExpect(jsonPath("$[1].idFood").value(2))
                .andExpect(jsonPath("$[1].name").value("baz"));

        verify(foodService, times(1)).getAllFood();
    }

    @Test
    public void testGetFoodByIdExisting() throws Exception {
        Integer idFood = 1;
        Food food = new Food();
        food.setIdFood(idFood);
        food.setName("bar");

        when(foodService.getFoodById(idFood)).thenReturn(Optional.of(food));

        mvc.perform(get("/food/{idFood}", idFood)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFood").value(idFood))
                .andExpect(jsonPath("$.name").value("bar"));

        verify(foodService, times(1)).getFoodById(idFood);
    }

    @Test
    public void testGetFoodByIdNonExisting() throws Exception {
        Integer idFood = 1;

        when(foodService.getFoodById(idFood)).thenReturn(Optional.empty());

        mvc.perform(get("/food/{idFood}", idFood))
                .andExpect(status().isNotFound());

        verify(foodService, times(1)).getFoodById(idFood);
    }

    @Test
    public void testAddFood() throws Exception {
        Integer idFood = 1;
        Food food = new Food();
        food.setIdFood(idFood);
        food.setName("bar");
        food.setPrice(BigDecimal.valueOf(2.5));
        food.setSize("Pack");

        when(foodService.addFood(any(Food.class))).thenReturn(food);
        mvc.perform(post("/food")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(food)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idFood").value(idFood))
                .andExpect(jsonPath("$.name").value("bar"))
                .andExpect(jsonPath("$.price").value(2.5))
                .andExpect(jsonPath("$.size").value("Pack"))
                .andExpect(header().string("Location", containsString("/food/1")));

        verify(foodService, times(1)).addFood(food);
    }

    @Test
    public void testUpdateFood() throws Exception {
        Integer idFood = 1;
        Food food = new Food();
        food.setIdFood(idFood);
        food.setName("Bar");
        food.setPrice(BigDecimal.valueOf(2.5));
        food.setSize("Pack");

        Food food2 = new Food();
        food2.setIdFood(idFood);
        food2.setName("Milk Bar");
        food2.setPrice(BigDecimal.valueOf(3.5));
        food2.setSize("Big Pack");

        when(foodService.getFoodById(idFood)).thenReturn(Optional.of(food));
        when(foodService.addFood(food)).thenReturn(food);

        mvc.perform(patch("/food/{idFood}", idFood)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(food2)))
                .andExpect(status().isOk());
        assertEquals("Milk Bar", food.getName());
        assertEquals(BigDecimal.valueOf(3.5), food.getPrice());
        assertEquals("Big Pack", food.getSize());

        verify(foodService, times(1)).getFoodById(1);
    }

    @Test
    public void testPartialUpdateFood() throws Exception {
        Integer idFood = 1;
        Food food = new Food();
        food.setIdFood(idFood);
        food.setName("Milk Bar");
        food.setPrice(BigDecimal.valueOf(2.5));
        food.setSize("Big Pack");

        Food food2 = new Food();
        food2.setIdFood(idFood);
        food2.setName(null);
        food2.setPrice(BigDecimal.valueOf(3.5));
        food2.setSize(null);

        when(foodService.getFoodById(idFood)).thenReturn(Optional.of(food));
        when(foodService.addFood(food)).thenReturn(food);

        mvc.perform(patch("/food/{idFood}",idFood)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(food2)))
                .andExpect(status().isOk());

        assertEquals("Milk Bar", food.getName());
        assertEquals(BigDecimal.valueOf(3.5),food.getPrice());
       assertEquals("Big Pack", food.getSize());

       verify(foodService, times(1)).getFoodById(idFood);
    }

    @Test
    public void testDeleteFood () throws Exception {
        Integer idFood = 1;
        Food food = new Food();
        food.setIdFood(idFood);

       when(foodService.getFoodById(idFood)).thenReturn(Optional.of(food));
       mvc.perform(delete("/food/{idFood}",idFood))
               .andExpect(status().isNoContent());

       verify(foodService, times(1)).delete(idFood);

    }
}
