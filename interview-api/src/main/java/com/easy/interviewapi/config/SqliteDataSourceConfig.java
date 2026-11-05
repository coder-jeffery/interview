package com.easy.interviewapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class SqliteDataSourceConfig {

    public SqliteDataSourceConfig(@Value("${spring.datasource.url}") String jdbcUrl) throws IOException {
        if (!jdbcUrl.startsWith("jdbc:sqlite:") || jdbcUrl.contains(":memory:")) {
            return;
        }
        String filePath = jdbcUrl.substring("jdbc:sqlite:".length());
        if (filePath.startsWith("file:")) {
            filePath = filePath.substring("file:".length());
            int query = filePath.indexOf('?');
            if (query >= 0) {
                filePath = filePath.substring(0, query);
            }
        }
        Path dbPath = Paths.get(filePath);
        Path parent = dbPath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }
}
