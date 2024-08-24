package pl.bnb.testControllers;

import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import pl.bnb.controllers.PasswordController;
import pl.bnb.entity.User;
import pl.bnb.services.PasswordService;
import pl.bnb.services.UserService;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PasswordController.class)
public class PasswordControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private PasswordService passwordService;
    @MockBean
    private UserService userService;

    @Test
    public void testBothSamePassword_Success() throws Exception {

        String password = "Password123";
        String samePassword = "Password123";
        String bookingNumber = "12345";

        when(passwordService.checkPassword(password, samePassword)).thenReturn(true);
        when(passwordService.updatePassword(bookingNumber, password)).thenReturn(true);

        mvc.perform(post("/checkPassword")
                        .param("password", password)
                        .param("samePassword", samePassword)
                        .sessionAttr("bookingNumber", bookingNumber))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/login.html"));
        verify(passwordService,times(1)).checkPassword(password, samePassword);
        verify(passwordService,times(1)).updatePassword(bookingNumber, password);
    }

    @Test
    public void testBothSamePasswordFail() throws Exception {

        when(passwordService.checkPassword(anyString(), anyString())).thenReturn(false);
        when(passwordService.getMessage()).thenReturn("Passwords do not match");

        mvc.perform(post("/checkPassword")
                        .param("password", "1234")
                        .param("samePassword", "4321"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("message", "Passwords do not match"));
        verify(passwordService, times(1)).checkPassword("1234", "4321");
    }
    @Test
    public void testSetPassword_Success() throws Exception {
        String bookingNumber = "12345";
        String password = "NewPassword";

        when(passwordService.updatePassword(bookingNumber, password)).thenReturn(true);

        mvc.perform(patch("/password")
                        .param("bookingNumber", bookingNumber)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().string("Password updated successfully."));
        verify(passwordService, times(1)).updatePassword(bookingNumber, password);
    }

    @Test
    public void testSetPassword_UserNotFound() throws Exception {
        String bookingNumber = "12345";
        String password = "Password12345";

        when(passwordService.updatePassword(bookingNumber, password)).thenReturn(false);

        mvc.perform(patch("/password")
                        .param("bookingNumber", bookingNumber)
                        .param("password", password))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found."));
        verify(passwordService, times(1)).updatePassword(bookingNumber, password);
    }
}