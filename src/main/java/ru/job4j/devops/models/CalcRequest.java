package ru.job4j.devops.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class representing a calculation request.
 * Contains two numeric arguments and the operation to perform.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalcRequest {
    private double first;
    private double second;
    private Operation operation;
} 