package com.mcpserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot Application Entry Point
 * Startet den MCP Server
 */
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }

    /**
     * ObjectMapper Bean für JSON Serialisierung/Deserialisierung
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
