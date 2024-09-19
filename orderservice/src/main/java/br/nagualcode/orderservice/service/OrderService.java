package br.nagualcode.orderservice.service;

import br.nagualcode.orderservice.model.Order;
import br.nagualcode.orderservice.model.OrderItem;
import br.nagualcode.orderservice.model.OrderMessage;
import br.nagualcode.orderservice.repository.OrderRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final RabbitTemplate rabbitTemplate;
    private final OrderRepository orderRepository;
    private static final String ORDER_EXCHANGE = "order.exchange";
    private static final String ROUTING_KEY = "order.routingKey";

    @Autowired
    public OrderService(RabbitTemplate rabbitTemplate, OrderRepository orderRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.orderRepository = orderRepository;
    }

    // Create a new order
    public Order createOrder(Order order) {
        // Calculate the total price based on the order items
        double totalPrice = calculateTotalPrice(order.getOrderItems());
        order.setTotalPrice(totalPrice);

        // Save the order to the database
        Order savedOrder = orderRepository.save(order);

        // Prepare an OrderMessage to send to RabbitMQ
        OrderMessage orderMessage = new OrderMessage(
            savedOrder.getId(),
            savedOrder.getCustomerName(),
            savedOrder.getCustomerEmail(),
            savedOrder.getStatus(),
            BigDecimal.valueOf(savedOrder.getTotalPrice())
        );

        // Send the order message to RabbitMQ
        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ROUTING_KEY, orderMessage);

        return savedOrder;
    }

    // Update an existing order (if needed)
    public Order updateOrder(Order order) {
        // Calculate the total price based on the order items
        double totalPrice = calculateTotalPrice(order.getOrderItems());
        order.setTotalPrice(totalPrice);

        // Save the updated order to the database
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

    // Listen to RabbitMQ for payment status updates
    @RabbitListener(queues = "order.update.queue")
    public void listenForPaymentUpdates(OrderMessage orderMessage) {
        // Extract orderId and status from the message
        Long orderId = orderMessage.getOrderId();
        String newStatus = orderMessage.getStatus(); // e.g., "paid"

        // Find the corresponding order and update its status
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(newStatus);
            orderRepository.save(order);
            System.out.println("Order status updated: " + orderId + " -> " + newStatus);
        } else {
            System.out.println("Order not found: " + orderId);
        }
    }

    // Helper method to calculate total price from the order items
    private double calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                         .mapToDouble(OrderItem::getPlatePrice)
                         .sum();
    }
}
