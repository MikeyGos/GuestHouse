package pl.bnb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bnb.entity.OrderProduct;
import pl.bnb.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {


    private final OrderRepository orderRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Optional<OrderProduct> findByBookingNumberAndIdDrinkAndIdFood(String bookingNumber, Integer idDrink, Integer idFood) {
        return orderRepository.findByBookingNumberAndIdDrinkAndIdFood(bookingNumber, idDrink, idFood);
    }

    public List<OrderProduct> getAllProduct(String bookingNumber) {
        return orderRepository.findByBookingNumber(bookingNumber);
    }

    public List<OrderProduct> saveOrders(List<OrderProduct> orderProducts) {
        return orderRepository.saveAll(orderProducts);
    }
    public void deleteOrder(OrderProduct orderProduct) {
        orderRepository.delete(orderProduct);
    }
}


    // stwórz jedną listę, jeśli użytkownik doda znów ten sam produkt, poszukaj produktu w liście i dodaje qty do tego które jest w bazie danych
    // jeśli
