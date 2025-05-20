package ru.job4j.devops.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.job4j.devops.models.CalcEvent;
import ru.job4j.devops.repository.CalcEventRepository;

@Component
@AllArgsConstructor
@Slf4j
public class CalcEventListener {

    private final CalcEventRepository calcEventRepository;

    @KafkaListener(topics = "calc_events", groupId = "job4j")
    public void handleCalcEvent(CalcEvent event) {
        log.debug("Received calculation event: {} {} {} = {}", 
            event.getFirst(), event.getType(), event.getSecond(), event.getResult());
        calcEventRepository.save(event);
    }
} 