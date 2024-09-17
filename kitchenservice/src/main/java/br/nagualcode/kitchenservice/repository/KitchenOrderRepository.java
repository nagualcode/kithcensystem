package br.nagualcode.kitchenservice.repository;

import br.nagualcode.kitchenservice.model.KitchenOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KitchenOrderRepository extends JpaRepository<KitchenOrder, Long> {
}
