package pl.bnb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.bnb.entity.User;

import java.util.Optional;

public interface LoginRepository extends JpaRepository<User, Integer> {
    Optional<User> findByBookingNumberAndPassword(String bookingNumber, String password);

    Optional<User> findByBookingNumber(String bookingNumber);
}
