package br.nagualcode.paymentservice.controller;

import br.nagualcode.paymentservice.model.Payment;
import br.nagualcode.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Update the payment status (either 'paid' or 'unpaid') for a specific orderId
    @PutMapping("/{orderId}")
    public ResponseEntity<Payment> updatePaymentStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {

        // Validate the status, it should only be 'paid' or 'unpaid'
        if (!status.equals("paid") && !status.equals("unpaid")) {
            return ResponseEntity.badRequest().build();
        }

        // Update the payment status in the PaymentService
        Payment payment = paymentService.updatePaymentStatus(orderId, status);

        // Return the updated payment or handle if the payment was not found
        if (payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
 // Retrieve all payments
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }


    // Retrieve the payment information for a specific orderId
    @GetMapping("/{orderId}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long orderId) {
        // Use the service to fetch the payment by orderId
        Optional<Payment> payment = paymentService.getPayment(orderId);

        // Return the payment if found, or respond with a 404 if not found
        return payment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
