package br.nagualcode.orderservice.service;

import br.nagualcode.orderservice.model.Order;
import br.nagualcode.orderservice.repository.OrderRepository;
import br.nagualcode.orderservice.messaging.OrderMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMessageProducer orderMessageProducer;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMessageProducer orderMessageProducer) {
        this.orderRepository = orderRepository;
        this.orderMessageProducer = orderMessageProducer;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(Order order) {
        // Save the new order to the database
        Order createdOrder = orderRepository.save(order);

        // Send a message to RabbitMQ after the order is created
        orderMessageProducer.sendOrderStatus(createdOrder);

        return createdOrder;
    }

    public Order updateOrderStatus(Long id, String status) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(status);
            Order updatedOrder = orderRepository.save(order);

            // Send a message to RabbitMQ when the order status changes
            orderMessageProducer.sendOrderStatus(updatedOrder);

            return updatedOrder;
        }
        return null;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
