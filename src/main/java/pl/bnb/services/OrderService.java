package pl.bnb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bnb.entity.OrderProduct;
import pl.bnb.repositories.OrderRepository;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public OrderProduct addOrder(OrderProduct orderProduct) {
        return orderRepository.save(orderProduct);
    }

    public List<OrderProduct> saveOrders(List<OrderProduct> orderProducts) {
        return orderRepository.saveAll(orderProducts);
    }
}