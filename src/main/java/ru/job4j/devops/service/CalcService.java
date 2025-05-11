package ru.job4j.devops.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.devops.models.CalcEvent;
import ru.job4j.devops.models.User;
import ru.job4j.devops.repository.CalcEventRepository;

import java.time.LocalDateTime;

/**
 * Service class for handling calculation operations.
 * Provides methods for performing mathematical operations and storing their results.
 * Each operation is recorded as a CalcEvent with associated user information.
 *
 * @author Maksim Levin
 * @see CalcEvent
 * @see User
 */
@Service
@AllArgsConstructor
public class CalcService {
    private final CalcEventRepository calcEventRepository;

    /**
     * Performs addition of two numbers and stores the operation details.
     * Creates a new CalcEvent record with the operation details, including
     * the user who performed the calculation, operands, result, and timestamp.
     *
     * @param user The user performing the calculation
     * @param first The first operand
     * @param second The second operand
     * @return CalcEvent containing the operation details and result
     */
    public CalcEvent add(User user, int first, int second) {
        int result = first + second;
        CalcEvent event = new CalcEvent();
        event.setUser(user);
        event.setFirst((double) first);
        event.setSecond((double) second);
        event.setResult((double) result);
        event.setCreateDate(LocalDateTime.now());
        event.setType("ADDITION");
        return calcEventRepository.save(event);
    }
} 