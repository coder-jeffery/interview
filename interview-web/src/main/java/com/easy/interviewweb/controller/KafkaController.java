package com.easy.interviewweb.controller;

import com.easy.interviewweb.kafka.KafkaMessage;
import com.easy.interviewweb.kafka.KafkaProduce;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class KafkaController {

    private final KafkaProduce kafkaProducer;
    private final ObjectMapper objectMapper;

    public KafkaController(KafkaProduce kafkaProducer, ObjectMapper objectMapper){
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/send")
    public void send(@RequestBody KafkaMessage kafkaMessage){
        kafkaProducer.send("demo_topic", objectMapper.writeValueAsString(kafkaMessage));
    }
}
