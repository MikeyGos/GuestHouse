package pl.bnb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bnb.entity.Food;

import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food,Integer> {
    Optional<Food> findByName(String name);
}
