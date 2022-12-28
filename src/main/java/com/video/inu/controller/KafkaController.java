package com.video.inu.controller;

import com.video.inu.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/kafka/test")
public class KafkaController {
    private final KafkaProducer producer;

    @PostMapping(value = "/message")
    public String sendMessage(@RequestParam("message") String message) {
        producer.sendMessage(message);

        return "success";
    }
}
