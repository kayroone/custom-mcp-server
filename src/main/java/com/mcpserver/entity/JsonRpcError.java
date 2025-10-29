package com.mcpserver.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonRpcError {

    private int code;

    private String message;

    private Object data;

    // Standard JSON-RPC Error Codes (aus der Spec)
    public static final int PARSE_ERROR = -32700;
    public static final int INVALID_REQUEST = -32600;
    public static final int METHOD_NOT_FOUND = -32601;
    public static final int INVALID_PARAMS = -32602;
    public static final int INTERNAL_ERROR = -32603;

    // Factory-Methoden für häufige Fehler
    public static JsonRpcError methodNotFound(String method) {
        return new JsonRpcError(METHOD_NOT_FOUND, "Method not found", method);
    }

    public static JsonRpcError invalidParams(String details) {
        return new JsonRpcError(INVALID_PARAMS, "Invalid params", details);
    }

    public static JsonRpcError internalError(String details) {
        return new JsonRpcError(INTERNAL_ERROR, "Internal error", details);
    }
}
