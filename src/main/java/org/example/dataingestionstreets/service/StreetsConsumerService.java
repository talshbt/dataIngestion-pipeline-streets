package org.example.dataingestionstreets.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dataingestionstreets.model.Street;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StreetsConsumerService {

    @KafkaListener(topics = "${kafka.topic.streets}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(List<String> streets) {
        System.out.println("Received " + streets.size() + " streets from Kafka:");
        streets.forEach(System.out::println);

        // You can now persist, map, or process the data here
    }
}