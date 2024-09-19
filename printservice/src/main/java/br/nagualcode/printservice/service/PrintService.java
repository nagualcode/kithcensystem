package br.nagualcode.printservice.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import br.nagualcode.printservice.model.OrderMessage;

@Service
public class PrintService {

    @RabbitListener(queues = "order.update.queue")
    public void listenForOrderUpdates(OrderMessage orderMessage) {
        if ("paid".equalsIgnoreCase(orderMessage.getStatus())) {
            System.out.println("Printing order for kitchen:");
            System.out.println("Order ID: " + orderMessage.getOrderId());
            System.out.println("Customer Name: " + orderMessage.getCustomer());
            // Simulate plates information (could be from another service or static for now)
            System.out.println("Plates: Pizza, Salad");
        }
    }
}
