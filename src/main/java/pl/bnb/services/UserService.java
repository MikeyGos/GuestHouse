package pl.bnb.services;

import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    public UserService(UserRepository userRepository, LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
        this.userRepository = userRepository;

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
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Integer idUser) {

        userRepository.deleteById(idUser);
    }


}
