package pl.bnb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bnb.entity.Drink;
import pl.bnb.repositories.DrinkRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DrinkService {

    private final DrinkRepository drinkRepository;

    @Autowired
    public DrinkService(DrinkRepository drinkRepository) {
        this.drinkRepository = drinkRepository;
    }

    public List<Drink> getAllDrink() {
        return drinkRepository.findAll();
    }

    public Optional<Drink> getDrinkById(Integer idDrink) {
        return drinkRepository.findById(idDrink);
    }

    public Drink addDrink(Drink drink) {
        return drinkRepository.save(drink);
    }

    public void deleteDrink(Integer idDrink) {
        drinkRepository.deleteById(idDrink);
    }
}
