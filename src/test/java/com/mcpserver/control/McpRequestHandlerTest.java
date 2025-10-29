package com.mcpserver.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcpserver.entity.JsonRpcRequest;
import com.mcpserver.entity.JsonRpcResponse;
import com.mcpserver.entity.Tool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für McpRequestHandler.
 */
class McpRequestHandlerTest {

    private McpRequestHandler handler;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        handler = new McpRequestHandler(objectMapper);
    }

    @Test
    void handleInitialize_ShouldReturnServerInfo() {
        // Given
        JsonRpcRequest request = new JsonRpcRequest();
        request.setId(1);
        request.setMethod("initialize");
        request.setParams(Map.of());

        // When
        JsonRpcResponse response = handler.handleRequest(request);

        // Then
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertNull(response.getError());
        assertNotNull(response.getResult());

        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) response.getResult();
        assertEquals("2024-11-05", result.get("protocolVersion"));

        @SuppressWarnings("unchecked")
        Map<String, Object> serverInfo = (Map<String, Object>) result.get("serverInfo");
        assertEquals("custom-mcp-server", serverInfo.get("name"));
        assertEquals("1.0.0", serverInfo.get("version"));
    }

    @Test
    void handleToolsList_ShouldReturnEchoTool() {
        // Given
        JsonRpcRequest request = new JsonRpcRequest();
        request.setId(2);
        request.setMethod("tools/list");
        request.setParams(Map.of());

        // When
        JsonRpcResponse response = handler.handleRequest(request);

        // Then
        assertNotNull(response);
        assertEquals(2, response.getId());
        assertNull(response.getError());

        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) response.getResult();
        @SuppressWarnings("unchecked")
        List<Tool> tools = (List<Tool>) result.get("tools");

        assertNotNull(tools);
        assertEquals(1, tools.size());

        Tool echoTool = tools.get(0);
        assertEquals("echo", echoTool.getName());
        assertEquals("Gibt den übergebenen Text zurück", echoTool.getDescription());
        assertNotNull(echoTool.getInputSchema());
    }

    @Test
    void handleToolsCall_WithEchoTool_ShouldReturnMessage() {
        // Given
        JsonRpcRequest request = new JsonRpcRequest();
        request.setId(3);
        request.setMethod("tools/call");
        request.setParams(Map.of(
                "name", "echo",
                "arguments", Map.of("message", "Hello World")
        ));

        // When
        JsonRpcResponse response = handler.handleRequest(request);

        // Then
        assertNotNull(response);
        assertEquals(3, response.getId());
        assertNull(response.getError());

        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) response.getResult();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> content = (List<Map<String, Object>>) result.get("content");

        assertNotNull(content);
        assertEquals(1, content.size());

        Map<String, Object> contentItem = content.get(0);
        assertEquals("text", contentItem.get("type"));
        assertEquals("Echo: Hello World", contentItem.get("text"));
    }

    @Test
    void handleToolsCall_WithUnknownTool_ShouldReturnError() {
        // Given
        JsonRpcRequest request = new JsonRpcRequest();
        request.setId(4);
        request.setMethod("tools/call");
        request.setParams(Map.of(
                "name", "unknown",
                "arguments", Map.of()
        ));

        // When
        JsonRpcResponse response = handler.handleRequest(request);

        // Then
        assertNotNull(response);
        assertEquals(4, response.getId());
        assertNull(response.getResult());
        assertNotNull(response.getError());
        assertEquals(-32601, response.getError().getCode());
        assertTrue(response.getError().getData().toString().contains("unknown"));
    }

    @Test
    void handleRequest_WithUnknownMethod_ShouldReturnMethodNotFound() {
        // Given
        JsonRpcRequest request = new JsonRpcRequest();
        request.setId(5);
        request.setMethod("unknown/method");
        request.setParams(Map.of());

        // When
        JsonRpcResponse response = handler.handleRequest(request);

        // Then
        assertNotNull(response);
        assertEquals(5, response.getId());
        assertNull(response.getResult());
        assertNotNull(response.getError());
        assertEquals(-32601, response.getError().getCode());
        assertEquals("Method not found", response.getError().getMessage());
    }

    @Test
    void handleRequest_ShouldPreserveRequestId() {
        // Given
        String stringId = "request-abc-123";
        JsonRpcRequest request = new JsonRpcRequest();
        request.setId(stringId);
        request.setMethod("initialize");

        // When
        JsonRpcResponse response = handler.handleRequest(request);

        // Then
        assertEquals(stringId, response.getId());
    }
}
