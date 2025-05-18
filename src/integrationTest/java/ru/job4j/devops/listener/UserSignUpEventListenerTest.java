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
import ru.job4j.devops.models.User;
import ru.job4j.devops.repository.UserRepository;

import java.time.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class UserSignUpEventListenerTest {
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    private static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("apache/kafka:3.7.2")
    );

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        kafka.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
        kafka.stop();
    }

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void whenSignupNewMember() {
        var user = new User();
        user.setName("Job4j new member : " + System.nanoTime());
        kafkaTemplate.send("signup", user);
        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    var optionalUser = userRepository.findByName(user.getName());
                    assertThat(optionalUser).isPresent();
                });
    }
}