package com.crisi.easy.service;

import com.crisi.easy.model.User;
import com.crisi.easy.repository.SnapshotWriter;
import com.crisi.easy.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DbChangeListener {

    private final SnapshotWriter snapshotWriter;
    private final ObjectMapper mapper = new ObjectMapper();

    private final UserRepository userRepository;

    public DbChangeListener(SnapshotWriter snapshotWriter, UserRepository userRepository) {
        this.snapshotWriter = snapshotWriter;
        this.userRepository = userRepository;
    }


    @JmsListener(destination = "db.change.queue")
    public void onMessage(String json) throws JsonProcessingException {
        JsonNode root = mapper.readTree(json);

        String table = root.get("table").asText();
        snapshotWriter.writeSnapshot(table, new ArrayList<>(userRepository.findAll()));
    }
}