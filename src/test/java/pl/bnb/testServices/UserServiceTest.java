package pl.bnb.testServices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.bnb.entity.User;
import pl.bnb.repositories.LoginRepository;
import pl.bnb.repositories.UserRepository;
import pl.bnb.services.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private LoginRepository loginRepository;            //can't be UR, LR, PE under one @Mock have to be separate
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetAllUser() {
        User user = new User();
        user.setIdUser(1);
        User user2 = new User();
        user2.setIdUser(1);

        List<User> user1 = List.of(user, user2);

        when(userRepository.findAll()).thenReturn(user1);

        List<User> allUser = userService.getAllUser();

        assertFalse(allUser.isEmpty());
        assertEquals(user1.size(), allUser.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        Integer idUser = 1;
        User user = new User();
        user.setIdUser(idUser);

        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByID(idUser);

        assertTrue(result.isPresent());
        assertEquals(idUser, result.get().getIdUser());
        verify(userRepository, times(1)).findById(idUser);
    }

    @Test
    public void testFindByIdNotFound() {
        Integer idUser = 1;
        when(userRepository.findById(idUser)).thenReturn(Optional.empty());
        Optional<User> result = userService.findByID(idUser);
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(idUser);
    }

    @Test
    public void testFindByBookingNumber() {
        String bookingNumber = "1234";
        User user = new User();
        user.setBookingNumber(bookingNumber);

        when(loginRepository.findByBookingNumber(bookingNumber)).thenReturn(Optional.of(user));
        Optional<User> result = userService.findByBookingNumber(bookingNumber);

        assertTrue(result.isPresent());
        verify(loginRepository, times(1)).findByBookingNumber(bookingNumber);
    }

    @Test
    public void testFindByBookingNumberNotFound() {
        String bookingNumber = "1234";
        when(loginRepository.findByBookingNumber(bookingNumber)).thenReturn(Optional.empty());
        Optional<User> number = userService.findByBookingNumber(bookingNumber);

        assertFalse(number.isPresent());
        verify(loginRepository, times(1)).findByBookingNumber(bookingNumber);
    }

    @Test
    public void testCreateUserWithoutPassword() {
        User user = new User();
        user.setIdUser(1);
        user.setBookingNumber("1234");
        user.setPassword(null);
        user.setName("Michał");
        user.setLastName("Pielewski");
        user.setPhoneNumber("+483733636633");

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);
        assertNotNull(result);
        assertEquals(1, result.getIdUser());
        assertEquals("1234", result.getBookingNumber());
        assertNull(result.getPassword());
        assertEquals("Michał", result.getName());
        assertEquals("Pielewski", result.getLastName());
        assertEquals("+483733636633", result.getPhoneNumber());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testCreateUserWithPassword() {
        User user = new User();
        user.setIdUser(1);
        user.setBookingNumber("1234");
        user.setPassword("GreatPassword1");
        user.setName("Michał");
        user.setLastName("Pielewski");
        user.setPhoneNumber("+483733636633");

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals(1, result.getIdUser());
        assertEquals("1234", result.getBookingNumber());
        assertEquals(encodedPassword, result.getPassword());
        assertEquals("Michał", result.getName());
        assertEquals("Pielewski", result.getLastName());
        assertEquals("+483733636633", result.getPhoneNumber());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUser() {

        User user = new User();
        user.setIdUser(1);
        user.setPassword("");

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser(user);

        assertNotNull(result);
        assertEquals(encodedPassword, result.getPassword());
    }

    @Test
    public void testDeleteUser() {
        Integer idUser = 1;

        doNothing().when(userRepository).deleteById(idUser);

        assertDoesNotThrow(() -> userService.deleteUser(idUser));

        verify(userRepository, times(1)).deleteById(idUser);
    }

    @Test
    public void testPaymentUser(){
        User user = new User();
        user.setPayOnline(true);

        when(userRepository.save(user)).thenReturn(user);
        userService.paymentUser(user);

        assertEquals(true, user.getPayOnline());
        verify(userRepository, times(1)).save(user);
    }
}
