package com.mcpserver.entity;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für JsonRpcResponse Entity.
 */
class JsonRpcResponseTest {

    @Test
    void success_ShouldCreateSuccessResponse() {
        // Given
        Object id = 1;
        Map<String, String> result = Map.of("status", "ok");

        // When
        JsonRpcResponse response = JsonRpcResponse.success(id, result);

        // Then
        assertNotNull(response);
        assertEquals("2.0", response.getJsonrpc());
        assertEquals(id, response.getId());
        assertEquals(result, response.getResult());
        assertNull(response.getError());
    }

    @Test
    void error_ShouldCreateErrorResponse() {
        // Given
        Object id = 2;
        JsonRpcError error = JsonRpcError.methodNotFound("test");

        // When
        JsonRpcResponse response = JsonRpcResponse.error(id, error);

        // Then
        assertNotNull(response);
        assertEquals("2.0", response.getJsonrpc());
        assertEquals(id, response.getId());
        assertNull(response.getResult());
        assertEquals(error, response.getError());
    }

    @Test
    void success_WithStringId_ShouldWork() {
        // Given
        String stringId = "request-123";
        String result = "success";

        // When
        JsonRpcResponse response = JsonRpcResponse.success(stringId, result);

        // Then
        assertEquals(stringId, response.getId());
        assertEquals(result, response.getResult());
    }

    @Test
    void error_WithNullId_ShouldWork() {
        // Given
        JsonRpcError error = JsonRpcError.internalError("test");

        // When
        JsonRpcResponse response = JsonRpcResponse.error(null, error);

        // Then
        assertNull(response.getId());
        assertEquals(error, response.getError());
    }
}
