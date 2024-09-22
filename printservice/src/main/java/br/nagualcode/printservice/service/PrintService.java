package br.nagualcode.printservice.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class PrintService {

    // Listen to the RabbitMQ print queue
    @RabbitListener(queues = "order.print.queue")
    public void receivePrintMessage(String message) {
        // Simulate printing by printing the message to the console
        System.out.println("********************************");
        System.out.println(message.replace("\\n", "\n"));
        System.out.println("********************************");
    }
}
