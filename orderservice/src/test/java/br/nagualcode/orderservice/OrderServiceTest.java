package br.nagualcode.orderservice;

import br.nagualcode.orderservice.model.Order;
import br.nagualcode.orderservice.model.OrderItem;
import br.nagualcode.orderservice.repository.OrderRepository;
import br.nagualcode.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class OrderServiceTest {

    // PostgreSQL container for the database
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    // RabbitMQ container
    @Container
    private static final RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3-management")
            .withExposedPorts(5672, 15672);  // RabbitMQ port and management UI port

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @DynamicPropertySource
    static void rabbitProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
    }

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
    }

    @Test
    void shouldCreateOrderAndSendMessageToRabbitMQ() {
        // Create and save a new order
        Order order = createSampleOrder();
        Order savedOrder = orderService.createOrder(order);

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getCustomerName()).isEqualTo("John Doe");

        // Verify that the message was sent to RabbitMQ
        String expectedMessage = String.format("Order %d status: %s", savedOrder.getId(), savedOrder.getStatus());
        rabbitTemplate.convertAndSend("order.exchange", "order.routingKey", expectedMessage);
    }

    @Test
    void shouldUpdateOrderStatusAndSendMessageToRabbitMQ() {
        // Create and save a new order
        Order order = createSampleOrder();
        Order savedOrder = orderService.createOrder(order);

        // Update the order status
        Order updatedOrder = orderService.updateOrderStatus(savedOrder.getId(), "paid");
        assertThat(updatedOrder.getStatus()).isEqualTo("paid");

        // Verify that the message was sent to RabbitMQ
        String expectedMessage = String.format("Order %d status: %s", updatedOrder.getId(), updatedOrder.getStatus());
        rabbitTemplate.convertAndSend("order.exchange", "order.routingKey", expectedMessage);
    }

    // Helper method to create a sample order
    private Order createSampleOrder() {
        OrderItem item1 = new OrderItem();
        item1.setPlateDescription("Spaghetti");
        item1.setPlatePrice(12.99);

        OrderItem item2 = new OrderItem();
        item2.setPlateDescription("Pizza");
        item2.setPlatePrice(9.99);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(item1);
        orderItems.add(item2);

        Order order = new Order();
        order.setCustomerName("John Doe");
        order.setCustomerEmail("john.doe@example.com");
        order.setStatus("pending");
        order.setOrderItems(orderItems);
        order.setTotalPrice(22.98);

        return order;
    }
}
