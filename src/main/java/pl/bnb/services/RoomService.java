package pl.bnb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bnb.entity.Room;
import pl.bnb.repositories.RoomRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }


    public List<Room> getAllRoomsByBookingNumber(String bookingNumber) {
        return roomRepository.findAllByBookingNumber(bookingNumber);
    }

    public List<Room> getReservationsByDate(LocalDate date) {
        return roomRepository.findByDate(date);
    }

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    public boolean hasExistingReservation(String bookingNumber, LocalDate date, String roomName) {
        List<Room> reservations = getReservationsByDate(date);
        return reservations.stream()
                .anyMatch(reservation -> reservation.getBookingNumber().equals(bookingNumber) && reservation.getRoomName().equals(roomName));
    }

    public void deleteRoom(Integer idParty) {
        if (roomRepository.existsById(idParty)) {
            roomRepository.deleteById(idParty);
        } else {
            throw new RuntimeException("Room with id " + idParty + " does not exist.");
        }
    }
}

