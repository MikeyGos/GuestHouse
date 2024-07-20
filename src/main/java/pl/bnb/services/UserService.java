package pl.bnb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bnb.entity.User;
import pl.bnb.repositories.LoginRepository;
import pl.bnb.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;



    @Autowired
    public UserService(UserRepository userRepository, LoginRepository loginRepository, PasswordEncoder passwordEncoder) {
        this.loginRepository = loginRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public Optional<User> findByID(Integer idUser) {
        return userRepository.findById(idUser);
    }

    public Optional<User> findByBookingNumber(String bookingNumber) {
        return loginRepository.findByBookingNumber(bookingNumber);
    }

    public User createUser(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return userRepository.save(user);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
    }

    public User updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(Integer idUser) {

        userRepository.deleteById(idUser);
    }


}
