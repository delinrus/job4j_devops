package ru.job4j.devops.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;
import ru.job4j.devops.models.Operation;

@Service
public class CalculatorService {

    private final Counter operationCounter;
    private final Counter divisionByZeroCounter;
    private final Timer operationTimer;

    public CalculatorService(MeterRegistry meterRegistry) {
        // Инициализация метрик
        this.operationCounter = meterRegistry.counter("calculator.operations");
        this.divisionByZeroCounter = meterRegistry.counter("calculator.errors.division_by_zero");
        this.operationTimer = meterRegistry.timer("calculator.operations.time");
    }

    public double add(double a, double b, Operation operation) {
        // Начало отслеживания времени выполнения операции
        Timer.Sample sample = Timer.start();
        double result = 0;

        switch (operation) {
            case ADD:
                result = a + b;
                break;
            case SUBTRACT:
                result = a - b;
                break;
            case MULTIPLY:
                result = a * b;
                break;
            case DIVIDE:
                if (b == 0) {
                    divisionByZeroCounter.increment();
                    throw new ArithmeticException("Cannot divide by zero");
                }
                result = a / b;
                break;
        }

        // Увеличиваем счетчик операций
        operationCounter.increment();
        // Завершаем отслеживание времени выполнения
        sample.stop(operationTimer);
        return result;
    }
}