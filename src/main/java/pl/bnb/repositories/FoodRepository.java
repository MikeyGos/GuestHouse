package pl.bnb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bnb.entity.Food;
@Repository
public interface FoodRepository extends JpaRepository<Food,Integer> {
}
