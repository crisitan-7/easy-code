package com.crisi.easy.repository;

import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;

@Component
public class SnapshotWriter {

    private static final String SNAPSHOT_DIR = "src/main/resources/snapshots";

    private Path getFile(String table) throws IOException {
        Files.createDirectories(Paths.get(SNAPSHOT_DIR));
        return Paths.get(SNAPSHOT_DIR, table + ".sql");
    }

    public synchronized void writeSnapshot(String table, List<Object> raws) {
        try {
            Path file = getFile(table);

            StringBuilder sb = new StringBuilder();
            for (Object raw : raws) {
                Field[] fields = raw.getClass().getDeclaredFields();
                StringBuilder columns = new StringBuilder();
                StringBuilder values = new StringBuilder();

                for (Field field : fields) {
                    field.setAccessible(true);
                    Object val = field.get(raw);

                    if (val == null || field.getName().equalsIgnoreCase("id"))
                        continue;

                    if (columns.length() > 0) {
                        columns.append(", ");
                        values.append(", ");
                    }
                    columns.append(field.getName());
                    if (val instanceof String) {
                        values.append("'").append(val.toString().replace("'", "''")).append("'");
                    } else {
                        values.append(val);
                    }
                }

                sb.append(String.format("INSERT INTO %s (%s) VALUES (%s);%n", table, columns, values));
            }

            Files.writeString(file, sb.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}