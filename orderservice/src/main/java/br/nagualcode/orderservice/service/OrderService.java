package br.nagualcode.orderservice.service;

import br.nagualcode.orderservice.model.Order;
import br.nagualcode.orderservice.model.OrderItem;
import br.nagualcode.orderservice.repository.OrderRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final RabbitTemplate rabbitTemplate;
    private final OrderRepository orderRepository;
    private static final String ORDER_EXCHANGE = "order.exchange";
    private static final String ROUTING_KEY = "order.routingKey";
    private static final String PRINT_QUEUE = "order.print.queue";

    @Autowired
    public OrderService(RabbitTemplate rabbitTemplate, OrderRepository orderRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.orderRepository = orderRepository;
    }

    // Create a new order
    public Order createOrder(Order order) {
        double totalPrice = calculateTotalPrice(order.getOrderItems());
        order.setTotalPrice(totalPrice);
        Order savedOrder = orderRepository.save(order);

        // Send the full Order entity to the order queue
        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ROUTING_KEY, savedOrder);

        return savedOrder;
    }

    // Update an existing order
    public Order updateOrder(Order order) {
        double totalPrice = calculateTotalPrice(order.getOrderItems());
        order.setTotalPrice(totalPrice);
        return orderRepository.save(order);
    }

    // Get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get order by ID
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // Update order status
    public Order updateOrderStatus(Long id, String status) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }

    // Delete an order
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    // Listen to RabbitMQ for payment status updates and send the full order to the print queue
    @RabbitListener(queues = "order.update.queue")
    public void listenForPaymentUpdates(Order updatedOrder) {
        Long orderId = updatedOrder.getId();
        String newStatus = updatedOrder.getStatus();

        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(newStatus);
            orderRepository.save(order);

            // Send the order to the print queue if the status is "paid"
            if ("paid".equalsIgnoreCase(newStatus)) {
                rabbitTemplate.convertAndSend(PRINT_QUEUE, order);
            }
        }
    }

    // Method to find orders by customer email
    public List<Order> findOrdersByEmail(String email) {
        return orderRepository.findByCustomerEmail(email);
    }

    // Helper method to calculate total price from the order items
    private double calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                         .mapToDouble(OrderItem::getPlatePrice)
                         .sum();
    }
}
