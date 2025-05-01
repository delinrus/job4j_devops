package ru.job4j.devops.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class representing the result of a calculation operation.
 * Contains a single numeric value that represents the result.
 */
@Data
@AllArgsConstructor
public class Result {
    private double value;

    /**
     * Default constructor for Result.
     */
    public Result() {
    }
}
