package br.nagualcode.orderservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    public static final String ORDER_QUEUE = "order.queue";
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_ROUTING_KEY = "order.routingKey";


    public static final String ORDER_UPDATE_QUEUE = "order.update.queue";
    public static final String ORDER_UPDATE_ROUTING_KEY = "order.update.routingKey";


    public static final String ORDER_PRINT_QUEUE = "order.print.queue";
    public static final String ORDER_PRINT_ROUTING_KEY = "order.print.routingKey"; // Use if needed


    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE);
    }


    @Bean
    public Queue orderUpdateQueue() {
        return new Queue(ORDER_UPDATE_QUEUE);
    }


    @Bean
    public Queue orderPrintQueue() {
        return new Queue(ORDER_PRINT_QUEUE, true);  
    }


    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

  
    @Bean
    public Binding orderBinding(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(ORDER_ROUTING_KEY);
    }


    @Bean
    public Binding orderUpdateBinding(Queue orderUpdateQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderUpdateQueue).to(orderExchange).with(ORDER_UPDATE_ROUTING_KEY);
    }


    @Bean
    public Binding orderPrintBinding(Queue orderPrintQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderPrintQueue).to(orderExchange).with(ORDER_PRINT_ROUTING_KEY);
    }


    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

  
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
