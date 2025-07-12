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
    
    // Отдельные счетчики для каждого типа операции
    private final Counter addCounter;
    private final Counter subtractCounter;
    private final Counter multiplyCounter;
    private final Counter divideCounter;

    public CalculatorService(MeterRegistry meterRegistry) {
        // Инициализация метрик
        this.operationCounter = meterRegistry.counter("calculator.operations");
        this.divisionByZeroCounter = meterRegistry.counter("calculator.errors.division_by_zero");
        this.operationTimer = meterRegistry.timer("calculator.operations.time");
        
        // Инициализация счетчиков для каждого типа операции
        this.addCounter = meterRegistry.counter("calculator.operations.add");
        this.subtractCounter = meterRegistry.counter("calculator.operations.subtract");
        this.multiplyCounter = meterRegistry.counter("calculator.operations.multiply");
        this.divideCounter = meterRegistry.counter("calculator.operations.divide");
    }

    public double add(double a, double b, Operation operation) {
        // Начало отслеживания времени выполнения операции
        Timer.Sample sample = Timer.start();
        double result = 0;

        switch (operation) {
            case ADD:
                result = a + b;
                addCounter.increment();
                break;
            case SUBTRACT:
                result = a - b;
                subtractCounter.increment();
                break;
            case MULTIPLY:
                result = a * b;
                multiplyCounter.increment();
                break;
            case DIVIDE:
                if (b == 0) {
                    divisionByZeroCounter.increment();
                    throw new ArithmeticException("Cannot divide by zer o");
                }
                result = a / b;
                divideCounter.increment();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + operation);
        }

        // Увеличиваем счетчик операций
        operationCounter.increment();
        // Завершаем отслеживание времени выполнения
        sample.stop(operationTimer);
        return result;
    }
}