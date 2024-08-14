package pl.bnb.testServices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bnb.entity.Room;
import pl.bnb.repositories.RoomRepository;
import pl.bnb.services.RoomService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;


    @Test
    public void testGetAllRoomsByBookingNumber() {
        String bookingNumber = "123";

        Room room = new Room();
        room.setBookingNumber(bookingNumber);
        room.setRoomName("Test Room");

        Room room2 = new Room();
        room2.setBookingNumber(bookingNumber);
        room2.setRoomName("Test Room2");
        List<Room> list = Arrays.asList(room, room2);

        when(roomRepository.findAllByBookingNumber(bookingNumber)).thenReturn(list);

        List<Room> result = roomService.getAllRoomsByBookingNumber(bookingNumber);

        assertFalse(result.isEmpty());
        verify(roomRepository, times(1)).findAllByBookingNumber(bookingNumber);
    }

    @Test
    public void testGetReservationsByDate() {
        LocalDate date = LocalDate.of(1994, 1, 14);
        Room room = new Room();
        room.setDate(date);
        Room room2 = new Room();
        room2.setDate(date);

        List<Room> list = Arrays.asList(room, room2);

        when(roomRepository.findByDate(date)).thenReturn(list);

        List<Room> reservationsByDate = roomService.getReservationsByDate(date);
        assertFalse(reservationsByDate.isEmpty());
        assertEquals(list.size(), reservationsByDate.size());
        verify(roomRepository, times(1)).findByDate(date);
    }

    @Test
    public void testSaveRoom() {
        Room room = new Room();
        room.setBookingNumber("12345");
        room.setDate(LocalDate.of(1994, 10, 1));
        room.setRoomName("Party Room");
        room.setTime(LocalTime.of(16, 0));

        when(roomRepository.save(room)).thenReturn(room);

        Room result = roomService.saveRoom(room);

        assertEquals("12345", result.getBookingNumber());
        assertEquals(LocalDate.of(1994, 10, 1), result.getDate());
        assertEquals("Party Room", result.getRoomName());
        assertEquals(LocalTime.of(16, 0), result.getTime());
    }

    @Test
    public void testHasExistingReservation() {
        String bookingNumber = "1234";
        LocalDate date = LocalDate.of(1994,10,15);
        String roomName = "Party Room";

        Room room = new Room();
        room.setBookingNumber(bookingNumber);
        room.setDate(date);
        room.setRoomName(roomName);

        when(roomRepository.findByDate(date)).thenReturn(List.of(room));

        boolean result = roomService.hasExistingReservation(bookingNumber, date, roomName);
        assertTrue(result);
    }

    @Test
    public void testHasExistingReservationNotFound(){
        String bookingNumber ="12345";
        LocalDate date = LocalDate.of(1993,12,11);
        String roomName = "Laundry room";

        when(roomRepository.findByDate(date)).thenReturn(List.of());

        boolean result = roomService.hasExistingReservation(bookingNumber, date, roomName);

        assertFalse(result);
    }

    @Test
    public void testDeletedRoom(){
        Integer idParty = 1;

        when(roomRepository.existsById(idParty)).thenReturn(true);

        assertDoesNotThrow(()-> roomService.deleteRoom(idParty));

        verify(roomRepository,times(1)).deleteById(idParty);
    }
    @Test
    public void testDeletedRoomDoesNotExist(){
        Integer idParty = 1;

        when(roomRepository.existsById(idParty)).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> roomService.deleteRoom(idParty));

        assertEquals("Room with id 1 does not exist.", exception.getMessage());
    }

}
