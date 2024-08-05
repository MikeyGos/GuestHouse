package pl.bnb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bnb.entity.PartyRoom;
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


    public List<PartyRoom> getAllRoomsByBookingNumber(String bookingNumber) {
        return roomRepository.findAllByBookingNumber(bookingNumber);
    }

    public List<PartyRoom> getReservationsByDate(LocalDate date) {
        return roomRepository.findByDate(date);
    }

    public PartyRoom saveRoom(PartyRoom partyRoom) {
        return roomRepository.save(partyRoom);
    }

    public boolean hasExistingReservation(String bookingNumber, LocalDate date, String roomName) {
        List<PartyRoom> reservations = getReservationsByDate(date);
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

