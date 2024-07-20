package pl.bnb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.bnb.entity.OrderProduct;

public interface OrderRepository extends JpaRepository<OrderProduct,Integer> {

}
