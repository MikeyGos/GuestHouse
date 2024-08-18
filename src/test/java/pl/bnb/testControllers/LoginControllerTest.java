package pl.bnb.testControllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.bnb.controllers.LoginController;
import pl.bnb.services.LoginService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LoginService loginService;

    @Test
    public void testLoginWithValidBNAndPass() throws Exception{
        String bookingNumber = "12345";
        String password = "password";

        when(loginService.validateUser(bookingNumber,password)).thenReturn(true);

        mvc.perform(post("/login.html")
                .param("bookingNumber",bookingNumber)
                .param("password",password))
                .andExpect(status().is3xxRedirection())             //is3xxRedirection(move to status 302) - in loginController is redirect:/index.html
                .andExpect(redirectedUrl("/index.html"));
    }

    @Test
    public void testLoginWithInvalidBNAndPass() throws Exception{
        String bookingNumber = "12345";
        String password = "Wrong password";

        when(loginService.validateUser(bookingNumber,password)).thenReturn(false);

        mvc.perform(post("/login.html")
                .param("bookingNumber",bookingNumber)
                .param("password",password))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testRegisterWithValidBN() throws Exception{
        String bookingNumber = "12345";

        when(loginService.validateUserToRegister(bookingNumber)).thenReturn(true);

        mvc.perform(post("/login.html")
                .param("bookingNumber",bookingNumber)
                .param("password",""))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void testRegisterWithInvalidBN() throws Exception{
        String bookingNumber = "12345";

        when(loginService.validateUserToRegister(bookingNumber)).thenReturn(false);

        mvc.perform(post("/login.html")
                .param("bookingNumber",bookingNumber)
                .param("password",""))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
}
