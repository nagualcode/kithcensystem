package br.nagualcode.printservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    @Bean
    public Queue orderUpdateQueue() {
        return new Queue("order.update.queue", true);
    }
}
