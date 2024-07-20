package pl.bnb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bnb.entity.User;
import pl.bnb.repositories.LoginRepository;

import java.util.Optional;

@Service
public class LoginService {
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(LoginRepository loginRepository, PasswordEncoder passwordEncoder) {
        this.loginRepository = loginRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean validateUser(String bookingNumber, String password) {
        Optional<User> loginNumber = loginRepository.findByBookingNumber(bookingNumber);
        if (loginNumber.isPresent()) {
            User user = loginNumber.get();
            return passwordEncoder.matches(password, user.getPassword());
        } else {
            return false;
        }
    }

    // User can't register the booking number, he will receive it via e-mail after confirming the room reservation.
    public boolean validateUserToRegister(String bookingNumber) {
        Optional<User> bN = loginRepository.findByBookingNumber(bookingNumber);
        if (bN.isPresent()) {
            User user = bN.get();
            if (user.getBookingNumber().equals(bookingNumber)) {
                String password1 = user.getPassword();
                return password1 == null || password1.isEmpty();
            }
        }
        return false;
    }
}
