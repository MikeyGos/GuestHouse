package pl.bnb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.bnb.entity.Drink;
import pl.bnb.services.DrinkService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/drink")
public class DrinkController {

    private final DrinkService drinkService;

    @Autowired
    public DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @GetMapping()
    public ResponseEntity<List<Drink>> getAllDrink() {
        List<Drink> allDrink = drinkService.getAllDrink();
        return ResponseEntity.ok(allDrink);
    }

    @GetMapping("/idDrink/{idDrink}")
    public ResponseEntity<Optional<Drink>> getDrinkByID(@PathVariable("idDrink") Integer idDrink) {
        Optional<Drink> drinkById = drinkService.getDrinkById(idDrink);
        return ResponseEntity.ok(drinkById);
    }
    @GetMapping("/nameDrink/{name}")
    public ResponseEntity<Optional<Drink>> getDrinkByName(@PathVariable("name")String name){
        Optional<Drink> drinkByName = drinkService.getDrinkByName(name);
        return ResponseEntity.ok(drinkByName);
    }

    @PostMapping()
    public ResponseEntity<Drink> addDrink(@RequestBody Drink drink) {
        Drink addedDrink = drinkService.addDrink(drink);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(drink.getIdDrink())
                .toUri();

        return ResponseEntity.created(location).body(addedDrink);
    }

    @PutMapping("/{idDrink}")
    public ResponseEntity<Drink> updateDrink(@PathVariable Integer idDrink, @RequestBody Drink drink) {
        return drinkService.getDrinkById(idDrink)
                .map(avibleDrink -> {
                    avibleDrink.setName(drink.getName());
                    avibleDrink.setPrice(drink.getPrice());
                    avibleDrink.setCapacity(drink.getCapacity());
                    return drinkService.addDrink(avibleDrink);
                })
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{idDrink}")
    public ResponseEntity<Drink> partialUpdateDrink(@PathVariable Integer idDrink, @RequestBody Drink drink) {
        return drinkService.getDrinkById(idDrink)
                .map(avibleDrink -> {
                    if (avibleDrink.getName() != null) avibleDrink.setName(drink.getName());
                    if (avibleDrink.getPrice() != null) avibleDrink.setPrice(drink.getPrice());
                    if (avibleDrink.getCapacity() != null) avibleDrink.setCapacity(drink.getCapacity());
                    return drinkService.addDrink(avibleDrink);
                })
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idDrink}")
    public ResponseEntity<Void> deleteDrink(@PathVariable Integer idDrink){
        drinkService.deleteDrink(idDrink);
       return ResponseEntity.noContent().build();
    }
}
