package pl.bnb.testControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.bnb.controllers.OrderController;
import pl.bnb.entity.OrderProduct;
import pl.bnb.order.BasketItem;
import pl.bnb.services.OrderService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testBasketBNWithBookingNumber() throws Exception {
        String bookingNumber = "12345";

        mvc.perform(get("/login")
                        .sessionAttr("bookingNumber", bookingNumber))
                .andExpect(status().isOk())
                .andExpect(content().string(bookingNumber));
    }

    @Test
    public void testGetAllProducts() throws Exception {
        String bookingNumber = "12345";

        when(orderService.getAllProduct(bookingNumber)).thenReturn(new ArrayList<>());

        mvc.perform(get("/{bookingNumber}", bookingNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(orderService, times(1)).getAllProduct(bookingNumber);
    }

    @Test
    public void testGetAllProductsWithoutBookingNumber() throws Exception {
        mvc.perform(get("/orderProduct"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));           //without booking number return empty string
    }

    @Test
    public void testGetAllProduct() throws Exception {
        String bookingNumber = "12345";
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setBookingNumber(bookingNumber);
        orderProduct.setNameProduct("Order1");
        orderProduct.setTotalPrice(BigDecimal.valueOf(10.0));
        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setBookingNumber(bookingNumber);
        orderProduct2.setNameProduct("Order2");
        orderProduct2.setTotalPrice(BigDecimal.valueOf(20.0));

        List<OrderProduct> products = List.of(orderProduct, orderProduct2);

        when(orderService.getAllProduct(bookingNumber)).thenReturn(products);

        mvc.perform(get("/orderProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr("bookingNumber", bookingNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products[0].bookingNumber").value(bookingNumber))
                .andExpect(jsonPath("$.products[0].nameProduct").value("Order1"))
                .andExpect(jsonPath("$.products[1].bookingNumber").value(bookingNumber))
                .andExpect(jsonPath("$.products[1].nameProduct").value("Order2"))
                        .andExpect(jsonPath("$.totalSum").value(30.00));
        verify(orderService, times(1)).getAllProduct(bookingNumber);
    }

    @Test
    public void testGetAllProductWithBookingNumberEmptyListOrder() throws Exception{
        String bookingNumber = "12345";

        when(orderService.getAllProduct(bookingNumber)).thenReturn(Collections.emptyList());

        mvc.perform(get("/orderProduct")
                .sessionAttr("bookingNumber",bookingNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isEmpty())
                .andExpect(jsonPath("$.totalSum").value(0.0));
        verify(orderService, times(1)).getAllProduct(bookingNumber);
    }
    @Test
    public void testSubmitOrder() throws Exception {
        String bookingNumber = "12345";

        BasketItem basketItem1 = new BasketItem();
        basketItem1.setIdDrink(1);
        basketItem1.setIdFood(2);
        basketItem1.setNameBasketProduct("Product1");
        basketItem1.setQtyBasketProduct(2);
        basketItem1.setTotalPrice(BigDecimal.valueOf(20.0));

        List<BasketItem> basket = List.of(basketItem1);

        // Przygotowanie odpowiednich obiektów OrderProduct
        OrderProduct orderProduct1 = new OrderProduct();            // added qty to existing order
        orderProduct1.setBookingNumber(bookingNumber);
        orderProduct1.setIdDrink(1);
        orderProduct1.setIdFood(2);
        orderProduct1.setNameProduct("Product1");
        orderProduct1.setQtyProduct(2);
        orderProduct1.setTotalPrice(BigDecimal.valueOf(20.0));

        OrderProduct orderProduct2 = new OrderProduct();            //create new order
        orderProduct2.setBookingNumber(bookingNumber);
        orderProduct2.setIdDrink(3);
        orderProduct2.setIdFood(4);
        orderProduct2.setNameProduct("Product2");
        orderProduct2.setQtyProduct(1);
        orderProduct2.setTotalPrice(BigDecimal.valueOf(10.0));

        List<OrderProduct> savedOrders = List.of(orderProduct1, orderProduct2);

        when(orderService.findByBookingNumberAndIdDrinkAndIdFood(bookingNumber, 1, 2))
                .thenReturn(Optional.of(orderProduct1));
        when(orderService.findByBookingNumberAndIdDrinkAndIdFood(bookingNumber, 3, 4))
                .thenReturn(Optional.empty());

        when(orderService.saveOrders(anyList())).thenReturn(savedOrders);

        mvc.perform(post("/orderProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr("bookingNumber", bookingNumber)
                        .content("[{\"idDrink\":1,\"idFood\":2,\"nameBasketProduct\":\"Product1\",\"qtyBasketProduct\":2,\"totalPrice\":20.0}," +
                                "{\"idDrink\":3,\"idFood\":4,\"nameBasketProduct\":\"Product2\",\"qtyBasketProduct\":1,\"totalPrice\":10.0}]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingNumber").value(bookingNumber))
                .andExpect(jsonPath("$[0].nameProduct").value("Product1"))
                .andExpect(jsonPath("$[0].qtyProduct").value(4))
                .andExpect(jsonPath("$[0].totalPrice").value(40.0))
                .andExpect(jsonPath("$[1].bookingNumber").value(bookingNumber))
                .andExpect(jsonPath("$[1].nameProduct").value("Product2"))
                .andExpect(jsonPath("$[1].qtyProduct").value(1))
                .andExpect(jsonPath("$[1].totalPrice").value(10.0));

        verify(orderService, times(1)).saveOrders(anyList());
    }

    @Test
    public void testDeleteExistingProduct() throws Exception {
        String bookingNumber = "12345";
        Integer idFood = 2;
        Integer idDrink = 1;

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setBookingNumber(bookingNumber);
        orderProduct.setIdFood(idFood);
        orderProduct.setIdDrink(idDrink);

        when(orderService.findByBookingNumberAndIdDrinkAndIdFood(bookingNumber,idDrink,idFood))
                .thenReturn(Optional.of(orderProduct));

        mvc.perform(delete("/orderProduct")
                .sessionAttr("bookingNumber", bookingNumber)
                .param("idDrink",idDrink.toString())
                .param("idFood",idFood.toString()))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteOrder(orderProduct);
    }

    @Test
    public void testDeleteProductWithoutBookingNumber() throws Exception {

        mvc.perform(delete("/orderProduct"))
                .andExpect(status().isNotFound());

        verify(orderService, never()).findByBookingNumberAndIdDrinkAndIdFood(anyString(),anyInt(),anyInt());
        verify(orderService, never()).deleteOrder(any());
    }
    @Test
    public void testDeleteProductWithoutIdDrinkOrIdFood() throws Exception {
        String bookingNumber = "12345";

        mvc.perform(delete("/orderProduct")
                .sessionAttr("bookingNumber", bookingNumber))
                .andExpect(status().isNotFound());

        verify(orderService, never()).findByBookingNumberAndIdDrinkAndIdFood(anyString(),any(),any());      //can't combine verbal with any
        verify(orderService, never()).deleteOrder(any());
    }

@Test
    public void testDeleteProductAndProductNotFound() throws Exception {
        String bookingNumber = "12345";
        Integer idDrink = 1;
        Integer idFood = 2;

        when(orderService.findByBookingNumberAndIdDrinkAndIdFood(bookingNumber,idDrink,idFood))
                .thenReturn(Optional.empty());

        mvc.perform(delete("/orderProduct")
                .sessionAttr("bookingNumber", bookingNumber)
                .param("idDrink",idDrink.toString())
                .param("idFood",idFood.toString()))
                .andExpect(status().isNotFound());

        verify(orderService, never()).deleteOrder(any());
}
}
