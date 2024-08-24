package pl.bnb.services;

import org.springframework.stereotype.Service;
import pl.bnb.entity.User;
import pl.bnb.repositories.LoginRepository;

import java.util.Optional;

@Service
public class PasswordService {
    private final LoginRepository loginRepository;
    private final UserService userService;
    private String message = "";

    public PasswordService(LoginRepository loginRepository, UserService userService) {
        this.loginRepository = loginRepository;
        this.userService = userService;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean checkPassword(String password, String samePassword) {
        int upperCaseCounter = 0;
        int digitCounter = 0;
        if (!(password.length() <= 20 && password.length() >= 8)) {
            setMessage("Password length should be between 8 and 20 characters");
            return false;
        }
        for (char c : password.toCharArray()) {
            if (Character.isWhitespace(c)) {
                setMessage("Password contains invalid characters");
                return false;
            }
            if (Character.isDigit(c)) {
                digitCounter++;
            }
            if (Character.isUpperCase(c)) {
                upperCaseCounter++;
            }
        }
        if (upperCaseCounter < 1 || digitCounter < 1) {
            setMessage("Password has to be at least 1 upper case letter or 1 number");
            return false;
        }
        if (!samePassword.equals(password)) {
            setMessage("Password does not match");
            return false;
        }
        setMessage("Password is correct, you can join to us right now.");
        return true;
    }
    public boolean updatePassword(String bookingNumber, String newPassword) {
        Optional<User> userOptional = userService.findByBookingNumber(bookingNumber);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(newPassword);
            userService.updateUser(user);
            return true;
        }
        return false;
    }


}
