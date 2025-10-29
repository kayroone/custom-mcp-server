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

    public JsonRpcRequest readRequest() throws IOException {
        String line = reader.readLine();

        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        log.debug("Received: {}", line);
        return objectMapper.readValue(line, JsonRpcRequest.class);
    }

    public void writeResponse(JsonRpcResponse response) throws IOException {
        String json = objectMapper.writeValueAsString(response);
        log.debug("Sending: {}", json);
        writer.println(json);
        writer.flush();
    }
}
