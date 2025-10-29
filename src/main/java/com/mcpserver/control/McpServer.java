package com.mcpserver.control;

import com.mcpserver.boundary.StdioMessageHandler;
import com.mcpserver.entity.JsonRpcRequest;
import com.mcpserver.entity.JsonRpcResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * MCP Server Main Loop.
 * <p>
 * Diese Klasse orchestriert den Request/Response-Zyklus des MCP Servers:
 * <ol>
 *   <li>Liest JSON-RPC Requests von stdin</li>
 *   <li>Delegiert an den McpRequestHandler zur Verarbeitung</li>
 *   <li>Schreibt JSON-RPC Responses nach stdout</li>
 * </ol>
 * Der Server läuft in einer Endlosschleife bis stdin geschlossen wird oder shutdown() aufgerufen wird.
 * </p>
 *
 * @see CommandLineRunner
 * @see McpRequestHandler
 * @see StdioMessageHandler
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class McpServer implements CommandLineRunner {

    private final StdioMessageHandler messageHandler;
    private final McpRequestHandler requestHandler;

    private volatile boolean running = true;

    /**
     * Startet die Server Main Loop.
     * <p>
     * Diese Methode wird automatisch von Spring Boot nach dem Start aufgerufen.
     * Sie läuft bis stdin geschlossen wird oder ein Fehler auftritt.
     * </p>
     *
     * @param args Kommandozeilen-Argumente (werden nicht verwendet)
     */
    @Override
    public void run(String... args) {
        log.info("MCP Server gestartet - Warte auf Requests...");

        try {
            while (running) {
                JsonRpcRequest request = messageHandler.readRequest();

                if (request == null) {
                    log.info("Input stream closed, shutting down...");
                    break;
                }

                JsonRpcResponse response = requestHandler.handleRequest(request);

                messageHandler.writeResponse(response);
            }
        } catch (Exception e) {
            log.error("Fehler im Server Main Loop", e);
        }

        log.info("MCP Server beendet");
    }

    /**
     * Stoppt den Server gracefully.
     * <p>
     * Setzt das running-Flag auf false, so dass die Main Loop beim nächsten
     * Durchlauf beendet wird.
     * </p>
     */
    public void shutdown() {
        log.info("Shutdown requested");
        running = false;
    }
}
