package pl.bnb.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.bnb.entity.Room;
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

    @GetMapping("/room/{bookingNumber}")
    public ResponseEntity<List<Room>> getAllRooms(@PathVariable String bookingNumber) {
        List<Room> allRoomsByBookingNumber = roomService.getAllRoomsByBookingNumber(bookingNumber);
        return ResponseEntity.ok(allRoomsByBookingNumber);
    }

    @GetMapping("/login")
    public String bookingNumberSession(HttpSession session) {
        return orderController.basketBN(session);
    }

    @PostMapping("/room")
    public ResponseEntity<String> createRoom(@RequestBody Room room) {
        boolean hasExistingReservation = roomService.hasExistingReservation(room.getBookingNumber(), room.getDate(), room.getRoomName());
        if (hasExistingReservation) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You already have a reservation for this room on this date.");
        }
        roomService.saveRoom(room);
        return ResponseEntity.ok("Room booked successfully.");
    }

    @GetMapping("/reservations")
    public List<Room> getReservations(@RequestParam String date) {
        return roomService.getReservationsByDate(LocalDate.parse(date));
    }

    @DeleteMapping("/room/{idParty}")
    public ResponseEntity<String> deleteRoom(@PathVariable Integer idParty) {
        try {
            roomService.deleteRoom(idParty);
            return ResponseEntity.ok("Room deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}