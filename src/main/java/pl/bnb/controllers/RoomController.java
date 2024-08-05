package pl.bnb.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.bnb.entity.PartyRoom;
import pl.bnb.services.RoomService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/bnb")
public class RoomController {

    private final RoomService roomService;
    private final OrderController orderController;

    @Autowired
    public RoomController(RoomService roomService, OrderController orderController) {
        this.roomService = roomService;
        this.orderController = orderController;
    }

    @GetMapping("/partyRoom/{bookingNumber}")
    public ResponseEntity<List<PartyRoom>> getAllRooms(@PathVariable String bookingNumber) {
        List<PartyRoom> allRoomsByBookingNumber = roomService.getAllRoomsByBookingNumber(bookingNumber);
        return ResponseEntity.ok(allRoomsByBookingNumber);
    }

    @GetMapping("/login")
    public String bookingNumberSession(HttpSession session, Model model) {
        return orderController.basketBN(session, model);
    }

    @PostMapping("/partyRoom")
    public ResponseEntity<String> createRoom(@RequestBody PartyRoom partyRoom) {
        boolean hasExistingReservation = roomService.hasExistingReservation(partyRoom.getBookingNumber(), partyRoom.getDate(), partyRoom.getRoomName());
        if (hasExistingReservation) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You already have a reservation for this room on this date.");
        }
        roomService.saveRoom(partyRoom);
        return ResponseEntity.ok("Room booked successfully.");
    }

    @GetMapping("/reservations")
    public List<PartyRoom> getReservations(@RequestParam String date) {
        return roomService.getReservationsByDate(LocalDate.parse(date));
    }

    @DeleteMapping("/partyRoom/{idParty}")
    public ResponseEntity<String> deleteRoom(@PathVariable Integer idParty) {
        try {
            roomService.deleteRoom(idParty);
            return ResponseEntity.ok("Room deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}