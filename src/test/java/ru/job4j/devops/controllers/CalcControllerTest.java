package ru.job4j.devops.controllers;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatusCode;
import ru.job4j.devops.models.Result;
import ru.job4j.devops.models.TwoArgs;
import ru.job4j.devops.service.CalculatorService;
import ru.job4j.devops.service.ResultFakeService;

import static org.assertj.core.api.Assertions.assertThat;

class CalcControllerTest {

    @Test
    public void whenOnePlusOneThenTwo() {
        var input = new TwoArgs(1, 1);
        var expected = new Result();
        expected.setResult(2D);
        
        // Create mock MeterRegistry
        MeterRegistry mockMeterRegistry = Mockito.mock(MeterRegistry.class);
        Counter mockCounter = Mockito.mock(Counter.class);
        Timer mockTimer = Mockito.mock(Timer.class);
        
        Mockito.when(mockMeterRegistry.counter(Mockito.anyString())).thenReturn(mockCounter);
        Mockito.when(mockMeterRegistry.timer(Mockito.anyString())).thenReturn(mockTimer);
        
        var output = new CalcController(
                new ResultFakeService(),
                new CalculatorService(mockMeterRegistry)
        ).summarise(input);
        assertThat(output.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        var result = output.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getResult()).isEqualTo(expected.getResult());
    }
}