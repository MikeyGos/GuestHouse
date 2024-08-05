package pl.bnb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.bnb.entity.PartyRoom;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<PartyRoom, Integer> {
    List<PartyRoom> findAllByBookingNumber(String bookingNumber);
    List<PartyRoom> findByDate(LocalDate date);
}
