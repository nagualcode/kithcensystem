package br.nagualcode.paymentservice.service;

import br.nagualcode.paymentservice.model.OrderMessage;
import br.nagualcode.paymentservice.model.Payment;
import br.nagualcode.paymentservice.repository.PaymentRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Listen to RabbitMQ queue and process the new order
    @RabbitListener(queues = "order.queue")
    public void processNewOrder(OrderMessage orderMessage) {
        // Simulate the creation of a payment
        Payment payment = new Payment();
        payment.setOrderId(orderMessage.getOrderId());
        payment.setStatus("unpaid"); // default status
        paymentRepository.save(payment);

        // Send an email to the customer with the order details (console log)
        sendOrderEmail(orderMessage);
    }

    // Simulate sending order confirmation email by logging to console
    private void sendOrderEmail(OrderMessage orderMessage) {
        System.out.println("---------------------------------");
        System.out.println("EMAIL: " + orderMessage.getEmail());
        System.out.println("SUBJECT: Your Order Details");
        System.out.println("Hello " + orderMessage.getCustomer() + ",\n\n" +
                "Thank you for your order. Here are the details:\n" +
                "Order ID: " + orderMessage.getOrderId() + "\n" +
                "Total Price: " + orderMessage.getTotal() + "\n\n");
        System.out.println("---------------------------------");
    }

    // Update the payment status (e.g., paid or unpaid)
    public Payment updatePaymentStatus(Long orderId, String status) {
        Optional<Payment> optionalPayment = paymentRepository.findByOrderId(orderId);

        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            payment.setStatus(status);
            paymentRepository.save(payment);

            // Send a message back to RabbitMQ if the status is 'paid'
            if ("paid".equals(status)) {
                sendPaymentUpdateToRabbitMQ(payment);
            }

            return payment;
        }
        return null; // If payment is not found, return null
    }

    // Send payment update back to RabbitMQ
    private void sendPaymentUpdateToRabbitMQ(Payment payment) {
        // Create a message to send to the order service
        OrderMessage message = new OrderMessage();
        message.setOrderId(payment.getOrderId());
        message.setStatus("paid"); // Update status to paid

        // Send message to RabbitMQ
        rabbitTemplate.convertAndSend("order.update.queue", message);
        System.out.println("Sent payment update to RabbitMQ for orderId: " + payment.getOrderId());
    }

    // Retrieve a payment by orderId
    public Optional<Payment> getPayment(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    // Retrieve all payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
