package br.nagualcode.kitchenservice.service;

import br.nagualcode.kitchenservice.model.KitchenOrder;
import br.nagualcode.kitchenservice.repository.KitchenOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KitchenOrderService {

    private final KitchenOrderRepository kitchenOrderRepository;

    public KitchenOrderService(KitchenOrderRepository kitchenOrderRepository) {
        this.kitchenOrderRepository = kitchenOrderRepository;
    }

    public List<KitchenOrder> getAllOrders() {
        return kitchenOrderRepository.findAll();
    }

    public Optional<KitchenOrder> getOrderById(Long id) {
        return kitchenOrderRepository.findById(id);
    }

    public KitchenOrder createOrder(KitchenOrder order) {
        return kitchenOrderRepository.save(order);
    }

    public KitchenOrder updateOrderStatus(Long id, String status) {
        Optional<KitchenOrder> order = kitchenOrderRepository.findById(id);
        if (order.isPresent()) {
            KitchenOrder kitchenOrder = order.get();
            kitchenOrder.setStatus(status);
            return kitchenOrderRepository.save(kitchenOrder);
        }
        return null;
    }
}
