package br.nagualcode.paymentservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import br.nagualcode.paymentservice.service.PaymentService;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_QUEUE = "order.queue";

    @Bean
    public Queue queue() {
        return new Queue(ORDER_QUEUE);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory,
                                                                   MessageListenerAdapter messageListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(queue());
        container.setMessageListener(messageListenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(PaymentService paymentService) {
        return new MessageListenerAdapter(paymentService, "processNewOrder");
    }
}
