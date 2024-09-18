package br.nagualcode.paymentservice.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import br.nagualcode.paymentservice.model.OrderMessage;
import br.nagualcode.paymentservice.model.Payment;
import br.nagualcode.paymentservice.repository.PaymentRepository;

import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PaymentRepository paymentRepository;

    // Listen to RabbitMQ queue and process the new order
    @RabbitListener(queues = "order.queue")
    public void processNewOrder(OrderMessage orderMessage) {
        // Simulate the creation of a payment
        Payment payment = new Payment();
        payment.setOrderId(orderMessage.getId());
        payment.setStatus("unpaid"); // default status
        paymentRepository.save(payment);

        // Send an email to the customer with the order details
        sendOrderEmail(orderMessage);
    }

    // Send order confirmation email
    private void sendOrderEmail(OrderMessage orderMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(orderMessage.getCustomerEmail());
        message.setSubject("Your Order Details");
        message.setText("Hello " + orderMessage.getCustomerName() + ",\n\n" +
                        "Thank you for your order. Here are the details:\n" +
                        "Order ID: " + orderMessage.getId() + "\n" +
                        "Total Price: " + orderMessage.getTotalPrice() + "\n\n" +
                        "Best regards,\n" +
                        "Kitchen Orders Team");

        mailSender.send(message);
    }

    // Update the payment status (e.g., paid or unpaid)
    public Payment updatePaymentStatus(Long orderId, String status) {
        Optional<Payment> optionalPayment = paymentRepository.findByOrderId(orderId);

        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            payment.setStatus(status);
            return paymentRepository.save(payment);
        }
        return null; // If payment is not found, return null
    }

    // Retrieve a payment by orderId
    public Optional<Payment> getPayment(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
}
