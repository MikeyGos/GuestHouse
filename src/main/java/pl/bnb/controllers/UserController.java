package pl.bnb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.bnb.entity.User;
import pl.bnb.services.UserService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUser() {
        List<User> allUser = userService.getAllUser();
        return ResponseEntity.ok(allUser);
    }

    @GetMapping("/iduser/{idUser}")
    public ResponseEntity<User> findByID(@PathVariable("idUser") Integer idUser) {
        Optional<User> byID = userService.findByID(idUser);
        return byID.stream()
                .map(ResponseEntity::ok)
                .findAny().orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/bookingNumber/{bookingNumber}")
    public ResponseEntity<User> findByBN(@PathVariable("bookingNumber")String bookingNumber){
        Optional<User> byBN = userService.findByBookingNumber(bookingNumber);
        return byBN.stream()
                .map(ResponseEntity::ok)
                .findAny().orElseGet(()->ResponseEntity.notFound().build());
    }


    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createduser = userService.createUser(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{idUser}")
                .buildAndExpand(user.getIdUser())
                .toUri();

        return ResponseEntity.created(location).body(createduser);
    }

    @PutMapping("/{idUser}")
    public ResponseEntity<User> updateUser(@PathVariable Integer idUser, @RequestBody User user) {
        return userService.findByID(idUser)
                .map(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setLastName(user.getLastName());
                    existingUser.setPhoneNumber(user.getPhoneNumber());
                    existingUser.setBookingNumber(user.getBookingNumber());
                    return userService.createUser(existingUser);
                })
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PatchMapping("/{idUser}")
    public ResponseEntity<User> partialUpdateUser(@PathVariable Integer idUser, @RequestBody User user) {

        return userService.findByID(idUser)
                .map(existingUser -> {
                    if (user.getName() != null) existingUser.setName(user.getName());
                    if (user.getLastName() != null) existingUser.setLastName(user.getLastName());
                    if (user.getPhoneNumber() != null) existingUser.setPhoneNumber(user.getPhoneNumber());
                    if (user.getBookingNumber() != null) existingUser.setBookingNumber(user.getBookingNumber());
                    return userService.updateUser(existingUser);
                })
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idUser}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer idUser) {
        userService.deleteUser(idUser);
        return ResponseEntity.noContent().build();
    }


}
