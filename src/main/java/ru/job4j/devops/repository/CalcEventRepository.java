package ru.job4j.devops.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.devops.models.CalcEvent;

/**
 * Repository interface for managing calculation events in the database.
 * Provides basic CRUD operations for CalcEvent entities.
 * Extends Spring Data's CrudRepository to inherit standard database operations.
 *
 * @author Maksim Levin
 * @see CalcEvent
 * @see org.springframework.data.repository.CrudRepository
 */
public interface CalcEventRepository extends CrudRepository<CalcEvent, Integer> {
} 