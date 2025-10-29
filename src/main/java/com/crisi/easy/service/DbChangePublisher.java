package com.crisi.easy.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DbChangePublisher {
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper mapper = new ObjectMapper();
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public DbChangePublisher(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void publishChange(String table, String operation, Object payload) {
        try {
            String json = mapper.writeValueAsString(Map.of(
                    "table", table,
                    "operation", operation,
                    "payload", payload
            ));
            log.info("Send message to queue");
            jmsTemplate.convertAndSend("db.change.queue", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}