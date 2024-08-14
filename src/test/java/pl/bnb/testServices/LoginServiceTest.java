package pl.bnb.testServices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.bnb.entity.User;
import pl.bnb.repositories.LoginRepository;
import pl.bnb.services.LoginService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private LoginRepository loginRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    @Test
   public void validateUserCorrectPasswordTest(){ //should return true
      String bookingNumber = "123456789";
      String password = "password";
      User user = new User();
      user.setBookingNumber(bookingNumber);
      user.setPassword("encodedPassword");

      when(loginRepository.findByBookingNumber(bookingNumber)).thenReturn(Optional.of(user));
      when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(true);

        boolean result = loginService.validateUser(bookingNumber,password);

        assertTrue(result);
    }

    @Test
   public void TestValidateUserIncorrectPassword(){   //should return false
      String bookingNumber ="1234567";
      String password = "password";
      User user = new User();
      user.setBookingNumber(bookingNumber);
      user.setPassword("encodedPassword");

      when(loginRepository.findByBookingNumber(bookingNumber)).thenReturn(Optional.of(user));
      when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(false);
      boolean result = loginService.validateUser(bookingNumber,password);

      assertFalse(result);
      // assertFalse when we expect false  -- assertTrue when we expect true
    }
    @Test
   public void testValidateUserNotFoundByBookingNumber(){ //should return false
        String bookingNumber = "123456789";
        User user = new User();
        user.setBookingNumber(bookingNumber);

        when(loginRepository.findByBookingNumber(user.getBookingNumber())).thenReturn(Optional.empty());

        boolean result = loginService.validateUserToRegister(bookingNumber);
        assertFalse(result);
    }

    @Test
    public void testValidateUserFoundByBookingNumber(){ //should return true
        String bookingNumber = "123456789";
        User user = new User();
        user.setBookingNumber(bookingNumber);

        when(loginRepository.findByBookingNumber(user.getBookingNumber())).thenReturn(Optional.of(user));

        boolean result = loginService.validateUserToRegister(bookingNumber);
        assertTrue(result);
    }
    @Test
    public void testValidateUserToRegisterNoPasswordSet(){
        String bookingNumber = "123456789";
        User user = new User();
        user.setBookingNumber(bookingNumber);
        user.setPassword(null);

        when(loginRepository.findByBookingNumber(bookingNumber)).thenReturn(Optional.of(user));

        boolean result = loginService.validateUserToRegister(bookingNumber);
        assertTrue(result);
    }

    @Test
    public  void testValidateUserToRegisterWrongPassword(){
        String bookingNumber = "123456789";
        User user = new User();
        user.setBookingNumber(bookingNumber);
        user.setPassword("wrongPassword");
        when(loginRepository.findByBookingNumber(bookingNumber)).thenReturn(Optional.of(user));
        boolean result = loginService.validateUserToRegister(bookingNumber);
        assertFalse(result);
    }
}
