package com.easy.interviewweb.kafka;

import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.stereotype.Service;

@Service
public class KafkaProduce {

    @Resource
    private KafkaProducer<String,String> kafkaProducer;

    public void send(String topic,String msg){
        kafkaProducer.send(new org.apache.kafka.clients.producer.ProducerRecord<>(topic, msg), (metadata, exception) -> {
            if(exception != null){
                exception.printStackTrace();
            }
        });
    }
}
