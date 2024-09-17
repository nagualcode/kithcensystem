package br.nagualcode.kitchenservice.controller;

import br.nagualcode.kitchenservice.model.KitchenOrder;
import br.nagualcode.kitchenservice.service.KitchenOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kitchen/orders")
public class KitchenController {

    private final KitchenOrderService kitchenOrderService;

    public KitchenController(KitchenOrderService kitchenOrderService) {
        this.kitchenOrderService = kitchenOrderService;
    }

    @GetMapping
    public List<KitchenOrder> getAllOrders() {
        return kitchenOrderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<KitchenOrder> getOrderById(@PathVariable Long id) {
        return kitchenOrderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public KitchenOrder createOrder(@RequestBody KitchenOrder order) {
        return kitchenOrderService.createOrder(order);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<KitchenOrder> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        KitchenOrder updatedOrder = kitchenOrderService.updateOrderStatus(id, status);
        return updatedOrder != null ? ResponseEntity.ok(updatedOrder) : ResponseEntity.notFound().build();
    }
}
