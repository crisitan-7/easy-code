package com.crisi.easy.service;

import com.crisi.easy.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DbSnapshotScheduler {

    private final UserRepository userRepo;
    private final DbChangePublisher publisher;

    public DbSnapshotScheduler(UserRepository userRepo, DbChangePublisher publisher) {
        this.userRepo = userRepo;
        this.publisher = publisher;
    }

    @Scheduled(fixedDelay = 60000)
    public void syncIfDbChanged() {
        List<?> allUsers = userRepo.findAll();
        publisher.publishChange("users", "SYNC", allUsers);
    }
}