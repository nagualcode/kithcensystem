package br.nagualcode.paymentservice.controller;

import br.nagualcode.paymentservice.model.Payment;
import br.nagualcode.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PutMapping("/{orderId}")
    public ResponseEntity<Payment> updatePaymentStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {

        if (!status.equals("paid") && !status.equals("unpaid")) {
            return ResponseEntity.badRequest().build();
        }

        Payment payment = paymentService.updatePaymentStatus(orderId, status);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long orderId) {
        Optional<Payment> payment = paymentService.getPayment(orderId);
        return payment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
