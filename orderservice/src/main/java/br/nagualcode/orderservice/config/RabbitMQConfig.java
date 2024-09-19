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

    // Constants for Payment Status Queue and Exchange
    public static final String ORDER_UPDATE_QUEUE = "order.update.queue";
    public static final String ORDER_UPDATE_ROUTING_KEY = "order.update.routingKey";

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

    // Direct exchange for orders and payments
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    // Binding for orders (order.queue -> order.exchange with routing key)
    @Bean
    public Binding orderBinding(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(ORDER_ROUTING_KEY);
    }

    // Binding for payment status updates (order.update.queue -> order.exchange with routing key)
    @Bean
    public Binding orderUpdateBinding(Queue orderUpdateQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderUpdateQueue).to(orderExchange).with(ORDER_UPDATE_ROUTING_KEY);
    }

    // Message converter to use JSON format
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate for sending messages
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
