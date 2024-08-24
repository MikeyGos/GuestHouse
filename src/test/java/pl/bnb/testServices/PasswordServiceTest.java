package pl.bnb.testServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bnb.entity.User;
import pl.bnb.repositories.LoginRepository;
import pl.bnb.services.PasswordService;
import pl.bnb.services.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class PasswordServiceTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private PasswordService passwordService;

    @Test
    public void testPasswordToShort(){
        String password = "123";
        String samePassword = "123";

        assertFalse(passwordService.checkPassword(password,samePassword));
        assertEquals("Password length should be between 8 and 20 characters",passwordService.getMessage());
    }

    @Test
    public void testPasswordToLong(){
        String password = "123456789000987654321";
        String samePassword = "123456789000987654321";

        assertFalse(passwordService.checkPassword(password,samePassword));
        assertEquals("Password length should be between 8 and 20 characters",passwordService.getMessage());
    }

    @Test
    public void testPasswordWithWhitespace(){
        String password = "This is Password";
        String samePassword = "This is Password";

        assertFalse(passwordService.checkPassword(password,samePassword));
        assertEquals("Password contains invalid characters",passwordService.getMessage());
    }

    @Test
    public void testPasswordMissingUpperCase(){
        String password = "greatpas1";
        String samePassword = "greatpas1";
        assertFalse(passwordService.checkPassword(password,samePassword));
        assertEquals("Password has to be at least 1 upper case letter or 1 number",passwordService.getMessage());
    }
    @Test
    public void testPasswordMissingNumber(){
        String password = "Greatpas";
        String samePassword = "Greatpas";
        assertFalse(passwordService.checkPassword(password,samePassword));
        assertEquals("Password has to be at least 1 upper case letter or 1 number",passwordService.getMessage());
    }
    @Test
    public void testNotTheSamePassword(){
        String password = "Greatpass1";
        String samePassword = "Greatpass";
        assertFalse(passwordService.checkPassword(password,samePassword));
        assertEquals("Password does not match",passwordService.getMessage());
    }

    @Test
    public void correctPassword(){
        String password = "CorrectPassword1";
        String samePassword = "CorrectPassword1";
        assertTrue(passwordService.checkPassword(password,samePassword));
        assertEquals("Password is correct, you can join to us right now.",passwordService.getMessage());
    }
    @Test
    public void testUpdatePassword_Success() {
        String bookingNumber = "12345";
        String newPassword = "newPassword";
        User user = new User();
        user.setBookingNumber(bookingNumber);
        user.setPassword("oldPassword");

        when(userService.findByBookingNumber(bookingNumber)).thenReturn(Optional.of(user));
        when(userService.updateUser(user)).thenReturn(user);

        boolean result = passwordService.updatePassword(bookingNumber, newPassword);

        assertTrue(result, "Password should be updated successfully.");
        assertEquals(newPassword, user.getPassword(), "The password should be updated to the new password.");
    }

}
