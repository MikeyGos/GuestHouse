package pl.bnb.testControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.bnb.controllers.UserController;
import pl.bnb.entity.User;
import pl.bnb.services.UserService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllUsers() throws Exception {
        User user = new User();
        user.setBookingNumber("12345");
        user.setName("Michał");
        user.setLastName("Pielewski");

        User user2 = new User();
        user2.setBookingNumber("56789");
        user2.setName("Karina");
        user2.setLastName("Mysza");

        List<User> userList = List.of(user, user2);

        when(userService.getAllUser()).thenReturn(userList);

        mvc.perform(get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingNumber").value("12345"))
                .andExpect(jsonPath("$[0].name").value("Michał"))
                .andExpect(jsonPath("$[0].lastName").value("Pielewski"))
                .andExpect(jsonPath("$[1].bookingNumber").value("56789"))
                .andExpect(jsonPath("$[1].name").value("Karina"))
                .andExpect(jsonPath("$[1].lastName").value("Mysza"));

        verify(userService, times(1)).getAllUser();
    }

    @Test
    public void findByIdSuccess() throws Exception {
        Integer idUser = 10;
        User user = new User();
        user.setIdUser(idUser);

        when(userService.findByID(idUser)).thenReturn(Optional.of(user));

        mvc.perform(get("/user/iduser/{idUser}", idUser)
                        .param("idUser", String.valueOf(idUser)))
                .andExpect(status().isOk());

        verify(userService, times(1)).findByID(idUser);
    }

    @Test
    public void findByIdFailed() throws Exception {
        Integer idUser = 1;

        when(userService.findByID(idUser)).thenReturn(Optional.empty());

        mvc.perform(get("/user/iduser/{idUser}", idUser)
                        .param("idUser", String.valueOf(idUser)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findByID(idUser);
    }

    @Test
    public void testFindByBNSuccess() throws Exception {
        String bookingNumber = "12345";
        User user = new User();
        user.setBookingNumber(bookingNumber);

        when(userService.findByBookingNumber(bookingNumber)).thenReturn(Optional.of(user));

        mvc.perform(get("/user/bookingNumber/{bookingNumber}", bookingNumber)
                        .param("bookingNumber", bookingNumber))
                .andExpect(status().isOk());

        verify(userService, times(1)).findByBookingNumber(bookingNumber);
    }

    @Test
    public void testFindByBnFailed() throws Exception {
        String bookingNumber = "12345";

        when(userService.findByBookingNumber(bookingNumber)).thenReturn(Optional.empty());

        mvc.perform(get("/user/bookingNumber/{bookingNumber}", bookingNumber)
                        .param("bookingNumber", bookingNumber))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findByBookingNumber(bookingNumber);
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setBookingNumber("12345");
        user.setName("Michał");
        user.setLastName("Pielewski");
        user.setPhoneNumber("1234567890");

        when(userService.createUser(user)).thenReturn(user);

        mvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(header().string("Location", containsString("/user/")))
                .andExpect(status().isCreated());

        verify(userService, times(1)).createUser(user);
    }

    @Test
    public void testUpdateUserSuccess() throws Exception {
        Integer idUser = 1;
        User user = new User();
        user.setName("Karina");
        user.setLastName("Mysia");
        user.setPhoneNumber("123456789");
        user.setBookingNumber("12345");

        User existingUser = new User();
        existingUser.setIdUser(idUser);
        existingUser.setName("Michał");
        existingUser.setLastName("Pielewski");
        existingUser.setPhoneNumber("98784747373");
        existingUser.setBookingNumber("54321");

        when(userService.findByID(idUser)).thenReturn(Optional.of(existingUser));
        when(userService.createUser(any(User.class))).thenReturn(user);

        mvc.perform(put("/user/{idUser}", idUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .param("idUser", String.valueOf(idUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.bookingNumber").value(user.getBookingNumber()));

        verify(userService, times(1)).findByID(idUser);
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    public void testUpdateUserNotFound() throws Exception {
        Integer idUser = 1;
        User user = new User();
        user.setName("Karina");
        user.setLastName("Mysia");
        user.setPhoneNumber("123456789");
        user.setBookingNumber("12345");

        when(userService.findByID(idUser)).thenReturn(Optional.empty());

        mvc.perform(put("/user/{idUser}" , idUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findByID(idUser);
    }

    @Test
    public void testPartialUpdateUserSuccess() throws Exception {
        Integer idUser = 1;
        User user = new User();
        user.setBookingNumber("12345");
        user.setName("Michał");

        User existingUser = new User();
        existingUser.setName("Karina");
        existingUser.setLastName("Mysia");
        existingUser.setBookingNumber("54321");
        existingUser.setPhoneNumber("1234566789999678");

        User updatedUser = new User();
        updatedUser.setPhoneNumber("1234566789999678");
        updatedUser.setName("Michał");
        updatedUser.setBookingNumber("12345");
        updatedUser.setLastName("Mysia");

        when(userService.findByID(idUser)).thenReturn(Optional.of(existingUser));
        when(userService.updateUser(any(User.class))).thenReturn(updatedUser);

        mvc.perform(patch("/user/{idUser}",idUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedUser.getName()))
                .andExpect(jsonPath("$.lastName").value(updatedUser.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(updatedUser.getPhoneNumber()))
                .andExpect(jsonPath("$.bookingNumber").value(updatedUser.getBookingNumber()));

        verify(userService,times(1)).updateUser(any(User.class));
        verify(userService,times(1)).findByID(idUser);
    }
    @Test
    public void testPartialUpdateUserNotFound() throws Exception {
        Integer idUser = 1;
        User user = new User();
        user.setLastName(null);
        user.setName("Michał");

        when(userService.findByID(idUser)).thenReturn(Optional.empty());

        mvc.perform(patch("/user/{idUser}" , idUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findByID(idUser);
        verify(userService, times(0)).updateUser(any(User.class));
    }
    @Test
    public void testDeleteUserSuccess() throws Exception{
        Integer idUser = 1;

        doNothing().when(userService).deleteUser(idUser);

        mvc.perform(delete("/user/{idUser}", idUser))
                .andExpect(status().isNoContent());

        verify(userService,times(1)).deleteUser(idUser);
    }
}
