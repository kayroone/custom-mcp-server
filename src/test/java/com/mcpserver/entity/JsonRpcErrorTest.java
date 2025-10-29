package com.mcpserver.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für JsonRpcError Entity.
 */
class JsonRpcErrorTest {

    @Test
    void methodNotFound_ShouldCreateCorrectError() {
        // Given
        String method = "unknown/method";

        // When
        JsonRpcError error = JsonRpcError.methodNotFound(method);

        // Then
        assertNotNull(error);
        assertEquals(-32601, error.getCode());
        assertEquals("Method not found", error.getMessage());
        assertEquals(method, error.getData());
    }

    @Test
    void invalidParams_ShouldCreateCorrectError() {
        // Given
        String details = "Missing required parameter";

        // When
        JsonRpcError error = JsonRpcError.invalidParams(details);

        // Then
        assertNotNull(error);
        assertEquals(-32602, error.getCode());
        assertEquals("Invalid params", error.getMessage());
        assertEquals(details, error.getData());
    }

    @Test
    void internalError_ShouldCreateCorrectError() {
        // Given
        String details = "NullPointerException";

        // When
        JsonRpcError error = JsonRpcError.internalError(details);

        // Then
        assertNotNull(error);
        assertEquals(-32603, error.getCode());
        assertEquals("Internal error", error.getMessage());
        assertEquals(details, error.getData());
    }

    @Test
    void constructor_ShouldCreateCustomError() {
        // Given
        int code = -32000;
        String message = "Custom error";
        String data = "Additional info";

        // When
        JsonRpcError error = new JsonRpcError(code, message, data);

        // Then
        assertEquals(code, error.getCode());
        assertEquals(message, error.getMessage());
        assertEquals(data, error.getData());
    }

    @Test
    void constants_ShouldHaveCorrectValues() {
        assertEquals(-32700, JsonRpcError.PARSE_ERROR);
        assertEquals(-32600, JsonRpcError.INVALID_REQUEST);
        assertEquals(-32601, JsonRpcError.METHOD_NOT_FOUND);
        assertEquals(-32602, JsonRpcError.INVALID_PARAMS);
        assertEquals(-32603, JsonRpcError.INTERNAL_ERROR);
    }
}
