package com.mcpserver.control;

import com.mcpserver.boundary.StdioMessageHandler;
import com.mcpserver.entity.JsonRpcRequest;
import com.mcpserver.entity.JsonRpcResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class McpServer implements CommandLineRunner {

    private final StdioMessageHandler messageHandler;
    private final McpRequestHandler requestHandler;

    private volatile boolean running = true;

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

    public void shutdown() {
        log.info("Shutdown requested");
        running = false;
    }
}
