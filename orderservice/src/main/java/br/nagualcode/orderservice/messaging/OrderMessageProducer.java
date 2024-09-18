package br.nagualcode.orderservice.messaging;

import br.nagualcode.orderservice.config.RabbitMQConfig;
import br.nagualcode.orderservice.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderMessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(OrderMessageProducer.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public OrderMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderStatus(Order order) {
        // Message object for RabbitMQ to include all necessary order details
        String message = String.format(
            "Order ID: %d, Status: %s, Customer: %s, Email: %s, Total: %.2f",
            order.getId(), order.getStatus(), order.getCustomerName(), order.getCustomerEmail(), order.getTotalPrice()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE, RabbitMQConfig.ORDER_ROUTING_KEY, message);
        logger.info("Sent message to RabbitMQ: {}", message);
    }
}
