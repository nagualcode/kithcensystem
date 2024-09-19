package br.nagualcode.printservice.service;

import br.nagualcode.printservice.model.OrderMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class PrintService {

    // Listen to the order.print.queue for new print jobs
    @RabbitListener(queues = "order.print.queue")
    public void listenForPrintOrders(OrderMessage orderMessage) {
        // Check if the order status is "paid" before printing
        if ("paid".equalsIgnoreCase(orderMessage.getStatus())) {
            // Print the details of the order
            System.out.println("Printing order for kitchen:");
            System.out.println("Order ID: " + orderMessage.getOrderId());
            System.out.println("Customer Name: " + orderMessage.getCustomer());
            System.out.println("Customer Email: " + orderMessage.getEmail());
            System.out.println("Total Price: " + orderMessage.getTotal());
            
            // Simulate printing the plates
            System.out.println("Plates:");
            orderMessage.getOrderItems().forEach(item -> {
                System.out.println("- " + item.getPlateDescription() + " (Price: " + item.getPlatePrice() + ")");
            });

            System.out.println("Order successfully printed!");
        } else {
            System.out.println("Order status is not 'paid', skipping print.");
        }
    }
}
