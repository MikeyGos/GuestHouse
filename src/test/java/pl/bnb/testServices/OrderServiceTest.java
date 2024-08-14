package pl.bnb.testServices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bnb.entity.OrderProduct;
import pl.bnb.repositories.OrderRepository;
import pl.bnb.services.OrderService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    @Test
    public void testFindByBookingNumberAndIdDrinkAndIdFood() { // should return Order Product
        String bookingNumber = "123";
        int idDrink = 1;
        int idFood = 2;
        OrderProduct orderProduct = new OrderProduct();

        orderProduct.setBookingNumber(bookingNumber);
        orderProduct.setIdDrink(idDrink);
        orderProduct.setIdFood(idFood);

        when(orderRepository.findByBookingNumberAndIdDrinkAndIdFood(orderProduct.getBookingNumber(),
                orderProduct.getIdDrink(), orderProduct.getIdFood())).thenReturn(Optional.of(orderProduct));

        Optional<OrderProduct> result = orderService.findByBookingNumberAndIdDrinkAndIdFood(orderProduct.getBookingNumber(),
                orderProduct.getIdDrink(), orderProduct.getIdFood());

        assertTrue(result.isPresent());
        assertEquals(orderProduct.getBookingNumber(), result.get().getBookingNumber());
        assertEquals(orderProduct.getIdDrink(), result.get().getIdDrink());
        assertEquals(orderProduct.getIdFood(), result.get().getIdFood());
    }

    @Test
    public void testGetAllProducts() {
        String bookingNumber = "123";
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setBookingNumber(bookingNumber);
        orderProduct.setIdOrder(1);
        orderProduct.setIdDrink(1);

        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setBookingNumber(bookingNumber);
        orderProduct2.setIdOrder(2);
        orderProduct2.setIdFood(1);

        List<OrderProduct> orderProducts = Arrays.asList(orderProduct, orderProduct2);

        when(orderRepository.findByBookingNumber(bookingNumber)).thenReturn(orderProducts);

        List<OrderProduct> result = orderService.getAllProduct(bookingNumber);
        assertFalse(result.isEmpty());
        assertEquals(orderProducts.size(), result.size());
        verify(orderRepository, times(1)).findByBookingNumber(bookingNumber);
    }


    @Test
    public void testSaveOrder() {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setBookingNumber("123");
        orderProduct.setIdOrder(1);
        orderProduct.setIdDrink(1);
        orderProduct.setNameProduct("Coca-cola");
        orderProduct.setQtyProduct(1);

        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setBookingNumber("123");
        orderProduct2.setIdOrder(2);
        orderProduct2.setIdFood(1);
        orderProduct2.setNameProduct("Bar");
        orderProduct2.setQtyProduct(1);

        List<OrderProduct> list = Arrays.asList(orderProduct, orderProduct2);

        when(orderRepository.saveAll(list)).thenReturn(list);

        List<OrderProduct> result = orderService.saveOrders(list);

        assertFalse(result.isEmpty());
        assertEquals(list.size(),result.size());
        verify(orderRepository,times(1)).saveAll(list);

    }

    @Test
    public void testDeleteOrder(){
        OrderProduct orderProduct = new OrderProduct();

        doNothing().when(orderRepository).delete(orderProduct);
        orderService.deleteOrder(orderProduct);
        verify(orderRepository, times(1)).delete(orderProduct);
    }
}
