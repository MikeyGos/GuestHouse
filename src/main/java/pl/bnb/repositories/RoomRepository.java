package pl.bnb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.bnb.entity.Room;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findAllByBookingNumber(String bookingNumber);
    List<Room> findByDate(LocalDate date);
}
