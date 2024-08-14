package pl.bnb.testServices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bnb.entity.Drink;
import pl.bnb.repositories.DrinkRepository;
import pl.bnb.services.DrinkService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class DrinkServiceTest {

    @Mock
    private DrinkRepository drinkRepository;

    @InjectMocks
    private DrinkService drinkService;


    @Test
    public void testGetAllDrinks() {
        Drink drink = new Drink();
        drink.setName("Fanta");
        Drink drink2 = new Drink();
        drink2.setName("Milk");

        List<Drink> drinks = Arrays.asList(drink, drink2);

        Mockito.when(drinkRepository.findAll()).thenReturn(drinks);

        List<Drink> allDrinkResult = drinkService.getAllDrink();

        assertEquals(2, allDrinkResult.size());
        assertEquals("Fanta", allDrinkResult.get(0).getName());
        assertEquals("Milk", allDrinkResult.get(1).getName());
    }

    @Test
    public void testGetDrinkById() {
        Drink drink = new Drink();
        drink.setIdDrink(1);
        drink.setName("Fanta");
        drink.setPrice(BigDecimal.valueOf(2.5));
        drink.setCapacity(500.00);

        Mockito.when(drinkRepository.findById(1)).thenReturn(Optional.of(drink));

        Optional<Drink> result = drinkService.getDrinkById(1);

        assertTrue(result.isPresent());

        assertEquals("Fanta", result.get().getName());
        assertEquals(BigDecimal.valueOf(2.5), result.get().getPrice());  // bigDecimal have to be valueOf
        assertEquals(500.00, result.get().getCapacity());
    }

    @Test
    public void testGetDrinkByName() {
        Drink drink = new Drink();
        drink.setName("Fanta");

        Mockito.when(drinkRepository.findByName("Fanta")).thenReturn(Optional.of(drink));

        Optional<Drink> result = drinkService.getDrinkByName("Fanta");

        assertTrue(result.isPresent());
        assertEquals("Fanta", result.get().getName());
    }

    @Test
    public void testAddDrink() {
        Drink drink = new Drink();
        drink.setIdDrink(1);
        drink.setName("Fanta");
        drink.setPrice(BigDecimal.valueOf(2.5));
        drink.setCapacity(500.00);
        drink.setQtyDrink(1);

        Mockito.when(drinkRepository.save(drink)).thenReturn(drink);

        Drink resultAddedDrink = drinkService.addDrink(drink);

        assertEquals("Fanta", resultAddedDrink.getName());
        assertEquals(BigDecimal.valueOf(2.5), resultAddedDrink.getPrice());
        assertEquals(500.00, resultAddedDrink.getCapacity());
        assertEquals(1, resultAddedDrink.getQtyDrink());

    }

    @Test
    public void testDeleteDrink() {
        drinkService.deleteDrink(1);
        Mockito.verify(drinkRepository).deleteById(1);
    }
}
