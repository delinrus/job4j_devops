package ru.job4j.devops.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.devops.models.Result;
import ru.job4j.devops.models.TwoArgs;
import ru.job4j.devops.service.ResultService;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for handling calculator operations.
 * Provides endpoints for basic mathematical operations.
 */
@RestController
@RequestMapping("calc")
@AllArgsConstructor
public class CalcController {

    private final ResultService resultService;

    /**
     * Endpoint for performing addition of two numbers.
     * @param twoArgs object containing two numeric arguments
     * @return ResponseEntity with the Result of the addition
     */
    @PostMapping("summarise")
    public ResponseEntity<Result> summarise(@RequestBody TwoArgs twoArgs) {
        var result = new Result();
        result.setFirstArg(twoArgs.getFirst());
        result.setSecondArg(twoArgs.getSecond());
        result.setResult(twoArgs.getFirst() + twoArgs.getSecond());
        result.setOperation("+");
        result.setCreateDate(LocalDate.now());
        resultService.save(result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/")
    public ResponseEntity<List<Result>> logs() {
        return ResponseEntity.ok(resultService.findAll());
    }
}
