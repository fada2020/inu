package com.video.inu.service;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KafkaConsumer {

   // @KafkaListener(topics = "${message.topic.name}", groupId = ConsumerConfig.GROUP_ID_CONFIG)
   @KafkaListener(
           topics = "my-topic",
           groupId = "foo"
   )
   public void listen(String msg) throws IOException {
       System.out.println(String.format("Consumed message : %s", msg));
   }
}
