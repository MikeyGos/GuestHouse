package pl.bnb.testControllers;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.bnb.controllers.OrderController;
import pl.bnb.controllers.PaymentController;
import pl.bnb.entity.User;
import pl.bnb.services.UserService;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService userService;
    @MockBean
    private OrderController orderController;
    @Mock
    private HttpSession session;

    @Test
    public void testPaymentMethodUserFound() throws Exception {
        User user = new User();
        user.setBookingNumber("12345");
        user.setPayOnline(true);

        when(orderController.basketBN(any(HttpSession.class))).thenReturn("12345");
        when(userService.findByBookingNumber("12345")).thenReturn(Optional.of(user));

        mvc.perform(post("/paymentMethod")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"payOnline\":true}")
                        .sessionAttr("session", session))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment method updated successfully"));

        verify(userService).paymentUser(user);
    }

    @Test
    public void testPaymentMethodUserNotFound() throws Exception {

        when(orderController.basketBN(any(HttpSession.class))).thenReturn("12345");
        when(userService.findByBookingNumber("12345")).thenReturn(Optional.empty());

        mvc.perform(post("/paymentMethod")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"payOnline\":true}")
                        .sessionAttr("session", session))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(userService, never()).paymentUser(any());
    }

    @Test
    public void testPaymentMethodBookingNumberNullOrEmpty() throws Exception {
        when(orderController.basketBN(any(HttpSession.class))).thenReturn("");

        mvc.perform(post("/paymentMethod")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"payOnline\":true}")
                        .sessionAttr("session", session))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(userService, never()).paymentUser(any());
    }
}
