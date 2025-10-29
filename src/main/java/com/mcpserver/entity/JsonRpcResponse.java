package com.mcpserver.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonRpcResponse {

    private String jsonrpc = "2.0";

    private Object id;

    private Object result;

    private JsonRpcError error;

    // Factory-Methode für erfolgreiche Antworten
    public static JsonRpcResponse success(Object id, Object result) {
        JsonRpcResponse response = new JsonRpcResponse();
        response.setId(id);
        response.setResult(result);
        return response;
    }

    // Factory-Methode für Fehler-Antworten
    public static JsonRpcResponse error(Object id, JsonRpcError error) {
        JsonRpcResponse response = new JsonRpcResponse();
        response.setId(id);
        response.setError(error);
        return response;
    }
}
