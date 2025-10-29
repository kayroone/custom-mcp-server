package com.mcpserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot Application Entry Point für den MCP Server.
 * <p>
 * Diese Klasse startet den Model Context Protocol (MCP) Server als Konsolenanwendung.
 * Der Server kommuniziert über stdin/stdout mit MCP Clients wie Claude Desktop.
 * </p>
 *
 * @see com.mcpserver.control.McpServer
 */
@SpringBootApplication
public class McpServerApplication {

    /**
     * Startet die Spring Boot Application.
     *
     * @param args Kommandozeilen-Argumente
     */
    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }

    /**
     * Erstellt den ObjectMapper Bean für JSON Serialisierung/Deserialisierung.
     * <p>
     * Diese Instanz wird von allen Komponenten verwendet, die JSON verarbeiten müssen.
     * </p>
     *
     * @return ObjectMapper Instanz für JSON-Verarbeitung
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
