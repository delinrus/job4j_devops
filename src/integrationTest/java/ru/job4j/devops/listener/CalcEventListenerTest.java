package ru.job4j.devops.listener;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import ru.job4j.devops.models.CalcEvent;
import ru.job4j.devops.models.User;
import ru.job4j.devops.repository.CalcEventRepository;
import ru.job4j.devops.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class CalcEventListenerTest {
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    private static final KafkaContainer KAFKA = new KafkaContainer(
            DockerImageName.parse("apache/kafka:3.7.2")
    );

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private CalcEventRepository calcEventRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    static void beforeAll() {
        POSTGRES.start();
        KAFKA.start();
    }

    @AfterAll
    static void afterAll() {
        POSTGRES.stop();
        KAFKA.stop();
    }

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Test
    void whenReceiveCalcEvent() {
        // Create a test user
        var user = new User();
        user.setName("TestUser");
        userRepository.save(user);

        // Create a test calculation event
        var event = new CalcEvent();
        event.setUser(user);
        event.setFirst(5.0);
        event.setSecond(3.0);
        event.setResult(8.0);
        event.setType("ADDITION");
        event.setCreateDate(LocalDateTime.now());

        // Send the event to Kafka
        kafkaTemplate.send("calc_events", event);

        // Wait and verify the event was saved
        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    List<CalcEvent> foundEvents = (List<CalcEvent>) calcEventRepository.findAll();
                    assertThat(foundEvents).hasSize(1);
                    var savedEvent = foundEvents.get(0);
                    assertThat(savedEvent.getUser().getId()).isEqualTo(user.getId());
                    assertThat(savedEvent.getFirst()).isEqualTo(5.0);
                    assertThat(savedEvent.getSecond()).isEqualTo(3.0);
                    assertThat(savedEvent.getResult()).isEqualTo(8.0);
                    assertThat(savedEvent.getType()).isEqualTo("ADDITION");
                    assertThat(savedEvent.getCreateDate()).isNotNull();
                });
    }
} 