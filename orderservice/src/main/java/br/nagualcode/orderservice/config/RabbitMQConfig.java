package br.nagualcode.orderservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Constants for Order Queue and Exchange
    public static final String ORDER_QUEUE = "order.queue";
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_ROUTING_KEY = "order.routingKey";

    // Constants for Payment Status Queue
    public static final String ORDER_UPDATE_QUEUE = "order.update.queue";
    public static final String ORDER_UPDATE_ROUTING_KEY = "order.update.routingKey";

    // New constant for Print Queue (no exchange/routing key needed)
    public static final String PRINT_QUEUE = "order.print.queue";

    // Queue for handling order messages
    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE);
    }

    // Queue for handling payment status updates
    @Bean
    public Queue orderUpdateQueue() {
        return new Queue(ORDER_UPDATE_QUEUE);
    }

    // Queue for handling print messages
    @Bean
    public Queue printQueue() {
        return new Queue(PRINT_QUEUE);
    }

    // Exchange for orders
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    // Binding for order queue
    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(ORDER_ROUTING_KEY);
    }

    // Binding for order update queue
    @Bean
    public Binding orderUpdateBinding() {
        return BindingBuilder.bind(orderUpdateQueue()).to(orderExchange()).with(ORDER_UPDATE_ROUTING_KEY);
    }

    // Message Converter
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Configure RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
