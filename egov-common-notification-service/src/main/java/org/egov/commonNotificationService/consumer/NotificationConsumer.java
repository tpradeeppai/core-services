package org.egov.commonNotificationService.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.egov.commonNotificationService.service.NotificationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class NotificationConsumer {

    private NotificationService notificationService;

    @Autowired
    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = {"${persister.update.tradelicense.topic}","${persister.save.tradelicense.topic}","${persister.update.tradelicense.workflow.topic}"})
    public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        try {
            log.info("Consuming record: " + record);

        } catch (final Exception e) {
            log.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
        }
        JSONObject json = new JSONObject(record);
        notificationService.process(json,topic);
    }
}
