package ru.job4j.devops.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.devops.models.Result;
import ru.job4j.devops.models.TwoArgs;

/**
 * REST controller for handling calculator operations.
 * Provides endpoints for basic mathematical operations.
 */
@RestController
@RequestMapping("calc")
public class CalcController {

    /**
     * Default constructor for CalcController.
     * Initializes the calculator controller.
     */
    public CalcController() {
    }

    /**
     * Endpoint for performing addition of two numbers.
     * @param twoArgs object containing two numeric arguments
     * @return ResponseEntity with the Result of the addition
     */
    @PostMapping("summarise")
    public ResponseEntity<Result> summarise(@RequestBody TwoArgs twoArgs) {
        var result = twoArgs.getFirst() + twoArgs.getSecond();
        return ResponseEntity.ok(new Result(result));
    }

    /**
     * Endpoint for performing multiplication of two numbers.
     * @param twoArgs object containing two numeric arguments
     * @return ResponseEntity with the Result of the multiplication
     */
    @PostMapping("times")
    public ResponseEntity<Result> times(@RequestBody TwoArgs twoArgs) {
        var result = twoArgs.getFirst() * twoArgs.getSecond();
        return ResponseEntity.ok(new Result(result));
    }
}
