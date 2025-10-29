package com.crisi.easy.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;

@Component
public class SnapshotLoader {

    private final JdbcTemplate jdbcTemplate;
    private static final String SNAPSHOT_DIR = "src/main/resources/snapshots";

    public SnapshotLoader(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void loadSnapshots() {
        try {
            Path dir = Paths.get(SNAPSHOT_DIR);
            if (!Files.exists(dir)) return;

            Files.list(dir)
                    .filter(path -> path.toString().endsWith(".sql"))
                    .forEach(this::executeFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void executeFile(Path file) {
        try {
            System.out.println("[SnapshotLoader] Loading " + file.getFileName());
            Files.lines(file)
                    .filter(line -> !line.trim().startsWith("--"))
                    .filter(line -> !line.trim().isEmpty())
                    .forEach(sql -> {
                        try {
                            jdbcTemplate.execute(sql);
                        } catch (Exception e) {
                            System.err.println("[SnapshotLoader] Error executing: " + sql);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}