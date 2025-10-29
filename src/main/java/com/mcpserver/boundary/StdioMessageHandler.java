package com.mcpserver.boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcpserver.entity.JsonRpcRequest;
import com.mcpserver.entity.JsonRpcResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Stdio Message Handler - Kommunikation über stdin/stdout.
 * <p>
 * Diese Klasse verwaltet die Kommunikation mit MCP Clients über Standard Input/Output.
 * Sie liest JSON-RPC Requests zeilenweise von stdin und schreibt JSON-RPC Responses nach stdout.
 * </p>
 * <p>
 * <strong>Wichtig:</strong> Alle Logs werden nach stderr geschrieben, um stdout sauber
 * zu halten (nur JSON-RPC Messages). Schreiben nach stdout würde die Kommunikation brechen.
 * </p>
 *
 * @see JsonRpcRequest
 * @see JsonRpcResponse
 */
@Slf4j
@Component
public class StdioMessageHandler {

    private final ObjectMapper objectMapper;
    private final BufferedReader reader;
    private final PrintWriter writer;

    public StdioMessageHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.writer = new PrintWriter(System.out, true);
    }

    /**
     * Liest eine JSON-RPC Request von stdin.
     * <p>
     * Blockiert bis eine Zeile verfügbar ist oder stdin geschlossen wird.
     * Leere Zeilen werden ignoriert.
     * </p>
     *
     * @return JsonRpcRequest Objekt oder null wenn stdin geschlossen wurde
     * @throws IOException Bei Lese-Fehlern
     */
    public JsonRpcRequest readRequest() throws IOException {
        String line = reader.readLine();

        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        log.debug("Received: {}", line);
        return objectMapper.readValue(line, JsonRpcRequest.class);
    }

    /**
     * Schreibt eine JSON-RPC Response nach stdout.
     * <p>
     * Die Response wird als JSON-String serialisiert und als einzelne Zeile geschrieben.
     * Nach dem Schreiben wird stdout geflusht, um sofortiges Senden sicherzustellen.
     * </p>
     *
     * @param response Die zu sendende JSON-RPC Response
     * @throws IOException Bei Schreib-Fehlern
     */
    public void writeResponse(JsonRpcResponse response) throws IOException {
        String json = objectMapper.writeValueAsString(response);
        log.debug("Sending: {}", json);
        writer.println(json);
        writer.flush();
    }
}
