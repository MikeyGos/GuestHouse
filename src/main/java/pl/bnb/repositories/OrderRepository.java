package pl.bnb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.bnb.entity.OrderProduct;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<OrderProduct,Integer> {
    List<OrderProduct> findByBookingNumber(String bookingNumber);
    Optional<OrderProduct> findByBookingNumberAndIdDrinkAndIdFood(String bookingNumber, Integer idDrink, Integer idFood);
    Optional<OrderProduct> findOrderProductByBookingNumber(String bookingNumber);
}
