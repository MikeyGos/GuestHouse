package pl.bnb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bnb.entity.User;
import pl.bnb.repositories.LoginRepository;

import java.util.Optional;

@Service
public class LoginService {
    private final LoginRepository loginRepository;

    @Autowired
    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public boolean validateUser(String bookingNumber, String password) {
        Optional<User> numberAndPassword = loginRepository.findByBookingNumberAndPassword(bookingNumber, password);
        if (numberAndPassword.isPresent()) {
            User user = numberAndPassword.get();
            return user.getBookingNumber().equals(bookingNumber) && user.getPassword().equals(password);
        } else {
            return false;
        }
    }

    // User can't register the booking number, he will receive it via e-mail after confirming the room reservation.
    public boolean validateUserToRegister(String bookingNumber) {
        Optional<User> numberAndPassword = loginRepository.findByBookingNumber(bookingNumber);
        if (numberAndPassword.isPresent()) {
            User user = numberAndPassword.get();
            if (user.getBookingNumber().equals(bookingNumber)) {
                String password1 = user.getPassword();
                return password1 == null || password1.isEmpty();
            }
        }
        return false;
    }
}
