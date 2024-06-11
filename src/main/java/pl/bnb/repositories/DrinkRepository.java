package pl.bnb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bnb.entity.Drink;
@Repository
public interface DrinkRepository extends JpaRepository<Drink,Integer> {
}
