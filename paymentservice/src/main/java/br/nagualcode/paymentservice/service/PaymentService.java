package br.nagualcode.paymentservice.service;

import br.nagualcode.paymentservice.model.Payment;
import br.nagualcode.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment updatePaymentStatus(Long orderId, String status) {
        Optional<Payment> paymentOptional = paymentRepository.findById(orderId);
        Payment payment;
        if (paymentOptional.isPresent()) {
            payment = paymentOptional.get();
            payment.setStatus(status);
        } else {
            payment = new Payment();
            payment.setOrderId(orderId);
            payment.setStatus(status);
        }
        return paymentRepository.save(payment);
    }

    public Optional<Payment> getPayment(Long orderId) {
        return paymentRepository.findById(orderId);
    }
}
