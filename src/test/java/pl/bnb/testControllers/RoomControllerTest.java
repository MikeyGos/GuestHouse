package pl.bnb.testControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.bnb.controllers.OrderController;
import pl.bnb.controllers.RoomController;
import pl.bnb.entity.Room;
import pl.bnb.repositories.RoomRepository;
import pl.bnb.services.RoomService;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RoomController.class)
public class RoomControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoomService roomService;

    @MockBean
    private OrderController orderController;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testGetAllRooms() throws Exception {
        String bookingNumber = "12345";

        Room room = new Room();
        room.setIdParty(1);
        room.setBookingNumber(bookingNumber);
        room.setRoomName("Party Room");

        Room room2 = new Room();
        room2.setIdParty(2);
        room2.setBookingNumber(bookingNumber);
        room2.setRoomName("Laundry Room");

        List<Room> allRoom = List.of(room, room2);
        when(roomService.getAllRoomsByBookingNumber(bookingNumber)).thenReturn(allRoom);

        mvc.perform(get("/bnb/room/{bookingNumber}" , bookingNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idParty").value(room.getIdParty()))
                .andExpect(jsonPath("$[1].idParty").value(room2.getIdParty()));

        verify(roomService, times(1)).getAllRoomsByBookingNumber(bookingNumber);
    }

    @Test
    public void testBookingNumberSession() throws Exception {
        when(orderController.basketBN(any(HttpSession.class))).thenReturn("12345");

        mvc.perform(get("/bnb/login"))
                .andExpect(status().isOk())
                .andExpect(content().string("12345"));

        verify(orderController, times(1)).basketBN(any(HttpSession.class));
    }
    @Test
    public void testCreateRoomSuccess() throws Exception {

        Room room = new Room();
        room.setBookingNumber("12345");
        room.setDate(LocalDate.of(2024, 10, 11));
        room.setRoomName("Party Room");

        when(roomService.hasExistingReservation(room.getBookingNumber(), room.getDate(), room.getRoomName())).thenReturn(false);
        when(roomService.saveRoom(any(Room.class))).thenReturn(room);

        mvc.perform(post("/bnb/room")           // if is AutoIncrement in base date CAN'T be /room/1 HAVE TO BE /room/
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/bnb/room/")))
                .andExpect(content().string("Room booked successfully."));

        verify(roomService, times(1)).saveRoom(any(Room.class));
    }

    @Test
    public void testGetReservationsSuccess() throws Exception {
        Room room = new Room();
        room.setBookingNumber("12345");
        room.setDate(LocalDate.of(2024, 10, 11));
        room.setRoomName("Party Room");
        Room room2 = new Room();
        room2.setBookingNumber("12345");
        room2.setDate(LocalDate.of(2024, 10, 11));
        room2.setRoomName("Laundry Room");

        List<Room> allRoom = List.of(room, room2);
        when(roomService.getAllRoomsByBookingNumber("12345")).thenReturn(allRoom);
        mvc.perform(get("/bnb/room/{bookingNumber}" , "12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idParty").value(room.getIdParty()))
                .andExpect(jsonPath("$[1].idParty").value(room2.getIdParty()));

        verify(roomService, times(1)).getAllRoomsByBookingNumber("12345");
    }

    @Test
    public void testDeleteRoomSuccess() throws Exception {
        Integer idParty = 1;

        doNothing().when(roomService).deleteRoom(idParty);
        mvc.perform(delete("/bnb/room/{idParty}", idParty))
                .andExpect(status().isOk());
        verify(roomService, times(1)).deleteRoom(idParty);
    }

    @Test
    public void testDeleteRoomNotFound() throws Exception {
        Integer idParty = 1;

        doNothing().when(roomService).deleteRoom(idParty);
        doThrow(new RuntimeException()).when(roomService).deleteRoom(idParty);
        mvc.perform(delete("/bnb/room/{idParty}",idParty))
                .andExpect(status().isNotFound());
        verify(roomService, times(1)).deleteRoom(idParty);
    }

}
