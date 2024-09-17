package br.nagualcode.userservice;

import br.nagualcode.userservice.model.User;
import br.nagualcode.userservice.repository.UserRepository;
import br.nagualcode.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class UserServiceTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUserSuccessfully() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        User savedUser = userService.createUser(user);
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("John Doe");
    }

    @Test
    void shouldRetrieveUserById() {
        User user = new User();
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");

        User savedUser = userService.createUser(user);
        Optional<User> foundUser = userService.getUserById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Jane Doe");
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        User user = new User();
        user.setName("Mark Doe");
        user.setEmail("mark.doe@example.com");

        User savedUser = userService.createUser(user);
        userService.deleteUser(savedUser.getId());

        Optional<User> foundUser = userService.getUserById(savedUser.getId());
        assertThat(foundUser).isEmpty();
    }
}
