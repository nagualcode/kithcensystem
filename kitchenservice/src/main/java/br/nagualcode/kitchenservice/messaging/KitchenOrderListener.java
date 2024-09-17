package br.nagualcode.kitchenservice.messaging;

import br.nagualcode.kitchenservice.model.KitchenOrder;
import br.nagualcode.kitchenservice.service.KitchenOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class KitchenOrderListener {

    private final KitchenOrderService kitchenOrderService;

    public KitchenOrderListener(KitchenOrderService kitchenOrderService) {
        this.kitchenOrderService = kitchenOrderService;
    }

    @RabbitListener(queues = "kitchen-orders")
    public void receiveMessage(Long orderId) {
        // Process the message and update the order status
        kitchenOrderService.updateOrderStatus(orderId, "cooking");
        System.out.println("Order " + orderId + " is now cooking.");
    }
}
