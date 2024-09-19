package br.nagualcode.orderservice.repository;

import br.nagualcode.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Query method to find orders by customerEmail
    List<Order> findByCustomerEmail(String customerEmail);
}
