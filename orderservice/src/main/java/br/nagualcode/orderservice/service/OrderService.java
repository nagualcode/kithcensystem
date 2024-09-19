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


        OrderMessage orderMessage = new OrderMessage(
                savedOrder.getId(),
                savedOrder.getCustomerName(),
                savedOrder.getCustomerEmail(),
                savedOrder.getStatus(),
                BigDecimal.valueOf(savedOrder.getTotalPrice())
        );

  
        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ROUTING_KEY, orderMessage);

        return savedOrder;
    }

 
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

   
    public Order updateOrderStatus(Long orderId, String status) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(status);
            return orderRepository.save(order);
        } else {
            throw new RuntimeException("Order not found with id: " + orderId);
        }
    }

  
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
    
    public List<Order> getOrdersByCustomerEmail(String customerEmail) {
        return orderRepository.findByCustomerEmail(customerEmail);
    }


    @RabbitListener(queues = "order.update.queue")
    public void handlePaymentStatusUpdate(OrderMessage orderMessage) {
        Long orderId = orderMessage.getOrderId();
        String newStatus = orderMessage.getStatus().toLowerCase();

    
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(newStatus);
            orderRepository.save(order);
            System.out.println("Order status updated: " + orderId + " -> " + newStatus);

         
            if ("paid".equalsIgnoreCase(newStatus)) {
                sendOrderToPrintQueue(order);
            }
        } else {
            System.out.println("Order not found: " + orderId);
        }
    }

  
    private void sendOrderToPrintQueue(Order order) {
        StringBuilder orderDetails = new StringBuilder();
        orderDetails.append("Order ID: ").append(order.getId()).append("\n")
                    .append("Customer Name: ").append(order.getCustomerName()).append("\n")
                    .append("Customer Email: ").append(order.getCustomerEmail()).append("\n")
                    .append("Total Price: ").append(order.getTotalPrice()).append("\n")
                    .append("Items:\n");

        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem item : orderItems) {
            orderDetails.append(" - ").append(item.toString()).append("\n");
        }

     
        rabbitTemplate.convertAndSend(PRINT_QUEUE, orderDetails.toString());
        System.out.println("Order sent to print queue: " + order.getId());
    }

  
    private double calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToDouble(OrderItem::getPlatePrice)  // Use the correct getter
                .sum();
    }

}
