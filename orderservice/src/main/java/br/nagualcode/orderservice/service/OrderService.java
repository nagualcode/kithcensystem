package br.nagualcode.orderservice.service;

import br.nagualcode.orderservice.messaging.OrderMessageProducer;
import br.nagualcode.orderservice.model.Order;
import br.nagualcode.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public Order updateOrderStatus(Long id, String status) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order existingOrder = orderOptional.get();
            existingOrder.setStatus(status);
            Order updatedOrder = orderRepository.save(existingOrder);
            
            // Send order status message to RabbitMQ
            orderMessageProducer.sendOrderStatus(updatedOrder);
            
            return updatedOrder;
        }
        return null;
    }
}
