package ru.job4j.devops.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class representing two numeric arguments for calculation operations.
 * Used as input for mathematical operations like addition and multiplication.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwoArgs {
    private double first;
    private double second;
}
