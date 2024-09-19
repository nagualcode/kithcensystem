package br.nagualcode.paymentservice.repository;

import br.nagualcode.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    

    Optional<Payment> findByOrderId(Long orderId);
}
