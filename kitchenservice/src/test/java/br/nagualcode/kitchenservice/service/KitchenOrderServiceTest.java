package br.nagualcode.kitchenservice.service;

import br.nagualcode.kitchenservice.model.KitchenOrder;
import br.nagualcode.kitchenservice.repository.KitchenOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class KitchenOrderServiceTest {

    @Autowired
    private KitchenOrderRepository kitchenOrderRepository;

    @Container
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        kitchenOrderRepository.deleteAll();
    }

    @Test
    void testCreateAndUpdateKitchenOrder() {
        // Create a new KitchenOrder
        KitchenOrder kitchenOrder = new KitchenOrder();
        kitchenOrder.setDescription("Pizza Margherita");
        kitchenOrder.setPrice(25.50);
        kitchenOrder.setStatus("pending");

        // Save it to the database
        KitchenOrder savedOrder = kitchenOrderRepository.save(kitchenOrder);

        // Verify that the KitchenOrder was saved
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getDescription()).isEqualTo("Pizza Margherita");
        assertThat(savedOrder.getPrice()).isEqualTo(25.50);
        assertThat(savedOrder.getStatus()).isEqualTo("pending");

        // Update the status of the KitchenOrder
        savedOrder.setStatus("cooking");
        KitchenOrder updatedOrder = kitchenOrderRepository.save(savedOrder);

        // Verify that the status was updated
        assertThat(updatedOrder.getStatus()).isEqualTo("cooking");
    }

    @Test
    void testDeleteKitchenOrder() {
        // Create and save a new KitchenOrder
        KitchenOrder kitchenOrder = new KitchenOrder();
        kitchenOrder.setDescription("Lasagna");
        kitchenOrder.setPrice(30.00);
        kitchenOrder.setStatus("pending");
        KitchenOrder savedOrder = kitchenOrderRepository.save(kitchenOrder);

        // Verify the order exists
        assertThat(kitchenOrderRepository.findById(savedOrder.getId())).isPresent();

        // Delete the order
        kitchenOrderRepository.delete(savedOrder);

        // Verify the order is deleted
        assertThat(kitchenOrderRepository.findById(savedOrder.getId())).isNotPresent();
    }
}
