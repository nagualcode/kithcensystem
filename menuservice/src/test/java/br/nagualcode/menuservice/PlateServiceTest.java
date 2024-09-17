package br.nagualcode.menuservice;

import br.nagualcode.menuservice.model.Plate;
import br.nagualcode.menuservice.repository.PlateRepository;
import br.nagualcode.menuservice.service.PlateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class PlateServiceTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private PlateService plateService;

    @Autowired
    private PlateRepository plateRepository;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        plateRepository.deleteAll();
    }

    @Test
    void shouldCreatePlateSuccessfully() {
        Plate plate = new Plate();
        plate.setDescription("Spaghetti Bolognese");
        plate.setPrice(12.99);

        Plate savedPlate = plateService.createPlate(plate);
        assertThat(savedPlate.getId()).isNotNull();
        assertThat(savedPlate.getDescription()).isEqualTo("Spaghetti Bolognese");
    }

    @Test
    void shouldRetrievePlateById() {
        Plate plate = new Plate();
        plate.setDescription("Margherita Pizza");
        plate.setPrice(9.99);

        Plate savedPlate = plateService.createPlate(plate);
        Optional<Plate> foundPlate = plateService.getPlateById(savedPlate.getId());

        assertThat(foundPlate).isPresent();
        assertThat(foundPlate.get().getDescription()).isEqualTo("Margherita Pizza");
    }

    @Test
    void shouldDeletePlateSuccessfully() {
        Plate plate = new Plate();
        plate.setDescription("Caesar Salad");
        plate.setPrice(7.99);

        Plate savedPlate = plateService.createPlate(plate);
        plateService.deletePlate(savedPlate.getId());

        Optional<Plate> foundPlate = plateService.getPlateById(savedPlate.getId());
        assertThat(foundPlate).isEmpty();
    }
}
