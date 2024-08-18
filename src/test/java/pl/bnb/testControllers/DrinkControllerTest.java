package pl.bnb.testControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.bnb.controllers.DrinkController;
import pl.bnb.entity.Drink;
import pl.bnb.services.DrinkService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DrinkController.class)
@AutoConfigureMockMvc(addFilters = false)               //to pass spring security error 401
public class DrinkControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DrinkService drinkService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllDrinks() throws Exception {
        Drink drink = new Drink();
        drink.setName("Fanta");
        drink.setPrice(BigDecimal.valueOf(2.5));
        drink.setCapacity(500.0);

        when(drinkService.getAllDrink()).thenReturn(List.of(drink));

        mvc.perform(get("/drink")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Fanta"))
                .andExpect(jsonPath("$[0].price").value(2.5))
                .andExpect(jsonPath("$[0].capacity").value(500.0));

        verify(drinkService, times(1)).getAllDrink();
    }

    @Test
    public void testGetDrinkById() throws Exception {
        Integer idDrink = 1;
        Drink drink = new Drink();
        drink.setIdDrink(idDrink);
        drink.setName("Fanta");
        drink.setPrice(BigDecimal.valueOf(2.5));

        when(drinkService.getDrinkById(idDrink)).thenReturn(Optional.of(drink));

        mvc.perform(get("/drink/idDrink/{idDrink}", idDrink)             //idDrink can be fixed or variable argument /drink/idDrink/1 without,idDrink
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDrink").value(idDrink))
                .andExpect(jsonPath("$.name").value("Fanta"))
                .andExpect(jsonPath("$.price").value(2.5));
        verify(drinkService, times(1)).getDrinkById(idDrink);
    }

    @Test
    public void testGetDrinkByName() throws Exception {
        String name = "Fanta";
        Drink drink = new Drink();
        drink.setIdDrink(1);
        drink.setName(name);
        drink.setPrice(BigDecimal.valueOf(2.5));
        drink.setCapacity(500.0);

        when(drinkService.getDrinkByName(name)).thenReturn(Optional.of(drink));

        mvc.perform(get("/drink/nameDrink/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDrink").value(1))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.price").value(2.5))
                .andExpect(jsonPath("$.capacity").value(500.0));

        verify(drinkService, times(1)).getDrinkByName(name);
    }

    @Test
    public void testAddDrink() throws Exception {
        Drink drink = new Drink();
        drink.setIdDrink(1);
        drink.setName("Fanta");
        drink.setPrice(BigDecimal.valueOf(2.5));
        drink.setCapacity(500.0);
        drink.setQtyDrink(1);

        when(drinkService.addDrink(any(Drink.class))).thenReturn(drink);

        mvc.perform(post("/drink")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(drink)))            //convert java to json
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idDrink").value(1))
                .andExpect(jsonPath("$.name").value("Fanta"))
                .andExpect(jsonPath("$.price").value(2.5))
                .andExpect(jsonPath("$.capacity").value(500.0))
                .andExpect(jsonPath("$.qtyDrink").value(1))
                .andExpect(header().string("Location", containsString("/drink/1")));

        verify(drinkService, times(1)).addDrink(drink);
    }

    @Test
    public void testUpdateDrinkExisting() throws Exception {
        Integer idDrink = 1;

        Drink existingDrink = new Drink();
        existingDrink.setIdDrink(1);
        existingDrink.setName("Fanta");
        existingDrink.setPrice(BigDecimal.valueOf(2.5));
        existingDrink.setCapacity(50.0);

        Drink updateDrink = new Drink();
        updateDrink.setIdDrink(1);
        updateDrink.setName("Cola");
        updateDrink.setPrice(BigDecimal.valueOf(3.5));
        updateDrink.setCapacity(500.0);

        when(drinkService.getDrinkById(idDrink)).thenReturn(Optional.of(existingDrink));
        when(drinkService.addDrink(existingDrink)).thenReturn(existingDrink);

        mvc.perform(put("/drink/{idDrink}", idDrink)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDrink)))
                .andExpect(status().isOk());

        assertEquals("Cola", existingDrink.getName());
        assertEquals(BigDecimal.valueOf(3.5), existingDrink.getPrice());
        assertEquals(500.0, existingDrink.getCapacity());

        verify(drinkService, times(1)).getDrinkById(idDrink);
    }

    @Test
    public void testUpdateDrinkNoExisting() throws Exception {
        Integer idDrink = 1;
        Drink existingDrink = new Drink();
        existingDrink.setIdDrink(idDrink);
        existingDrink.setName("Fanta");
        existingDrink.setPrice(BigDecimal.valueOf(2.5));
        existingDrink.setCapacity(50.0);

        when(drinkService.getDrinkById(idDrink)).thenReturn(Optional.empty());

        mvc.perform(put("/drink/{idDrink}", idDrink)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(existingDrink)))
                .andExpect(status().isNotFound());

        verify(drinkService , times(1)).getDrinkById(idDrink);
    }

    @Test
    public void testPartialUpdateDrink() throws Exception {
        Integer idDrink = 1;
        Drink existingDrink = new Drink();
        existingDrink.setIdDrink(idDrink);
        existingDrink.setName("Fanta");
        existingDrink.setPrice(BigDecimal.valueOf(2.5));
        existingDrink.setCapacity(50.0);

        Drink updateDrink = new Drink();
        updateDrink.setIdDrink(idDrink);
        updateDrink.setName("Cola");
        updateDrink.setPrice(null);
        updateDrink.setCapacity(null);


        when(drinkService.getDrinkById(idDrink)).thenReturn(Optional.of(existingDrink));
        when(drinkService.addDrink(existingDrink)).thenReturn(existingDrink);

        mvc.perform(patch("/drink/{idDrink}", idDrink)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDrink)))
                .andExpect(status().isOk());

        assertEquals("Cola", existingDrink.getName());
        assertEquals(BigDecimal.valueOf(2.5), existingDrink.getPrice());
        assertEquals(50.0, existingDrink.getCapacity());
        verify(drinkService, times(1)).getDrinkById(idDrink);
    }

    @Test
    public void testPartialUpdateDrinkNoExisting() throws Exception {
        Integer idDrink = 1;
        Drink existingDrink = new Drink();
        existingDrink.setIdDrink(idDrink);

        when(drinkService.getDrinkById(idDrink)).thenReturn(Optional.empty());

        mvc.perform(patch("/drink/{idDrink}", idDrink)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(existingDrink)))
                .andExpect(status().isNotFound());

        verify(drinkService , times(1)).getDrinkById(idDrink);
    }

    @Test
    public void testDeleteDrink() throws Exception {
        Integer idDrink = 1;
        Drink drink = new Drink();
        drink.setIdDrink(idDrink);

        when(drinkService.getDrinkById(idDrink)).thenReturn(Optional.of(drink));
        mvc.perform(delete("/drink/{idDrink}", idDrink))
                .andExpect(status().isNoContent());

        verify(drinkService, times(1)).deleteDrink(idDrink);
    }

}


