package br.nagualcode.printservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Define the print queue name
    public static final String PRINT_QUEUE = "order.print.queue";

    // Create the queue bean
    @Bean
    public Queue printQueue() {
        return new Queue(PRINT_QUEUE, true);
    }
}
