package com.water_services;

import java.util.Properties;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
public class WaterProducer {
	     @Autowired
	     private CustomKafkaTemplate<String, Object> kafkaTemplate;

	    public void push(String topic, Object value) {
	        kafkaTemplate.send(topic, value);
	    }

}
