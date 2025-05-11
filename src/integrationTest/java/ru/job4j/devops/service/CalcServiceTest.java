package ru.job4j.devops.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.job4j.devops.models.CalcEvent;
import ru.job4j.devops.models.User;
import ru.job4j.devops.repository.CalcEventRepository;
import ru.job4j.devops.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the CalcService class.
 * Uses TestContainers to provide an isolated PostgreSQL database for testing.
 * Tests the calculation service's ability to perform operations and store results
 * in the database.
 *
 * @author Maksim Levin
 * @see CalcService
 * @see CalcEvent
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CalcServiceTest {
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    ).withReuse(true);

    @Autowired
    private CalcService calcService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalcEventRepository calcEventRepository;

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    public void whenAddNumbers() {
        // Create a test user
        var user = new User();
        user.setName("TestUser");
        userRepository.save(user);

        // Perform addition
        var event = calcService.add(user, 5, 3);

        // Verify the event was saved
        var foundEvent = calcEventRepository.findById(event.getId());
        assertThat(foundEvent).isPresent();
        assertThat(foundEvent.get().getUser().getId()).isEqualTo(user.getId());
        assertThat(foundEvent.get().getFirst()).isEqualTo(5.0);
        assertThat(foundEvent.get().getSecond()).isEqualTo(3.0);
        assertThat(foundEvent.get().getResult()).isEqualTo(8.0);
        assertThat(foundEvent.get().getType()).isEqualTo("ADDITION");
        assertThat(foundEvent.get().getCreateDate()).isNotNull();
    }
} 