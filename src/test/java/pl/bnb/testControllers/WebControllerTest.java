package pl.bnb.testControllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import pl.bnb.controllers.WebController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(WebController.class)
public class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/index.html"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void testAbout() throws Exception {
        mockMvc.perform(get("/about.html"))
                .andExpect(status().isOk())
                .andExpect(view().name("about"));
    }

    @Test
    public void testReservation() throws Exception {
        mockMvc.perform(get("/reservation.html"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation"));
    }

    @Test
    public void testProducts() throws Exception {
        mockMvc.perform(get("/products.html"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"));
    }

    @Test
    public void testStore() throws Exception {
        mockMvc.perform(get("/store.html"))
                .andExpect(status().isOk())
                .andExpect(view().name("store"));
    }

    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(get("/login.html"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testFood() throws Exception {
        mockMvc.perform(get("/food.html"))
                .andExpect(status().isOk())
                .andExpect(view().name("food"));
    }

    @Test
    public void testDrinks() throws Exception {
        mockMvc.perform(get("/drinks.html"))
                .andExpect(status().isOk())
                .andExpect(view().name("drinks"));
    }

    @Test
    public void testRegister() throws Exception {
        mockMvc.perform(get("/register.html"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void testOrder() throws Exception {
        mockMvc.perform(get("/order.html"))
                .andExpect(status().isOk())
                .andExpect(view().name("order"));
    }

    @Test
    public void testUserOrder() throws Exception {
        mockMvc.perform(get("/basket.html"))
                .andExpect(status().isOk())
                .andExpect(view().name("basket"));
    }

    @Test
    public void testSnacks() throws Exception {
        mockMvc.perform(get("/snacks.html"))
                .andExpect(status().isOk())
                .andExpect(view().name("snacks"));
    }

    @Test
    public void testUserBasket() throws Exception {
        mockMvc.perform(get("/userbasket.html"))
                .andExpect(status().isOk())
                .andExpect(view().name("userbasket"));
    }

    @Test
    public void testPayment() throws Exception {
        mockMvc.perform(get("/payment.html"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment"));
    }

    @Test
    public void testPartyRoom() throws Exception {
        mockMvc.perform(get("/room.html"))
                .andExpect(status().isOk())
                .andExpect(view().name("room"));
    }

    @Test
    public void testRoomList() throws Exception {
        mockMvc.perform(get("/roomList.html"))
                .andExpect(status().isOk())
                .andExpect(view().name("roomList"));
    }
}