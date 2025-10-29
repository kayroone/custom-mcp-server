package com.mcpserver.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcpserver.entity.JsonRpcError;
import com.mcpserver.entity.JsonRpcRequest;
import com.mcpserver.entity.JsonRpcResponse;
import com.mcpserver.entity.Tool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class McpRequestHandler {

    private static final String METHOD_INITIALIZE = "initialize";
    private static final String METHOD_TOOLS_LIST = "tools/list";
    private static final String METHOD_TOOLS_CALL = "tools/call";
    private static final String SERVER_NAME = "custom-mcp-server";
    private static final String SERVER_VERSION = "1.0.0";
    private static final String PROTOCOL_VERSION = "2024-11-05";
    private static final String TOOL_ECHO_NAME = "echo";
    private static final String TOOL_ECHO_DESCRIPTION = "Gibt den übergebenen Text zurück";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_ARGUMENTS = "arguments";
    private static final String PARAM_MESSAGE = "message";
    private static final String RESULT_CONTENT = "content";
    private static final String CONTENT_TYPE = "type";
    private static final String CONTENT_TEXT = "text";
    private static final String CONTENT_TYPE_TEXT = "text";

    private final ObjectMapper objectMapper;

    public JsonRpcResponse handleRequest(JsonRpcRequest request) {
        log.info("Handling method: {}", request.getMethod());

        try {
            return switch (request.getMethod()) {
                case METHOD_INITIALIZE -> handleInitialize(request);
                case METHOD_TOOLS_LIST -> handleToolsList(request);
                case METHOD_TOOLS_CALL -> handleToolsCall(request);
                default -> JsonRpcResponse.error(
                        request.getId(),
                        JsonRpcError.methodNotFound(request.getMethod())
                );
            };
        } catch (Exception e) {
            log.error("Error handling request", e);
            return JsonRpcResponse.error(
                    request.getId(),
                    JsonRpcError.internalError(e.getMessage())
            );
        }
    }

    private JsonRpcResponse handleInitialize(JsonRpcRequest request) {
        log.info("Initialize request received");

        Map<String, Object> serverInfo = Map.of(
                "name", SERVER_NAME,
                "version", SERVER_VERSION
        );

        Map<String, Object> result = Map.of(
                "protocolVersion", PROTOCOL_VERSION,
                "serverInfo", serverInfo
        );

        return JsonRpcResponse.success(request.getId(), result);
    }

    private JsonRpcResponse handleToolsList(JsonRpcRequest request) {
        log.info("Tools list request received");

        Tool echoTool = Tool.builder()
                .name(TOOL_ECHO_NAME)
                .description(TOOL_ECHO_DESCRIPTION)
                .inputSchema(Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "message", Map.of(
                                        "type", "string",
                                        "description", "Der Text, der zurückgegeben werden soll"
                                )
                        ),
                        "required", List.of("message")
                ))
                .build();

        Map<String, Object> result = Map.of("tools", List.of(echoTool));
        return JsonRpcResponse.success(request.getId(), result);
    }

    private JsonRpcResponse handleToolsCall(JsonRpcRequest request) {
        log.info("Tools call request received");

        @SuppressWarnings("unchecked")
        Map<String, Object> params = objectMapper.convertValue(request.getParams(), Map.class);

        String toolName = (String) params.get(PARAM_NAME);

        @SuppressWarnings("unchecked")
        Map<String, Object> arguments = (Map<String, Object>) params.get(PARAM_ARGUMENTS);

        log.info("Calling tool: {} with arguments: {}", toolName, arguments);

        if (TOOL_ECHO_NAME.equals(toolName)) {
            String message = (String) arguments.get(PARAM_MESSAGE);

            Map<String, Object> result = Map.of(
                    RESULT_CONTENT, List.of(
                            Map.of(
                                    CONTENT_TYPE, CONTENT_TYPE_TEXT,
                                    CONTENT_TEXT, "Echo: " + message
                            )
                    )
            );

            return JsonRpcResponse.success(request.getId(), result);
        }

        return JsonRpcResponse.error(
                request.getId(),
                JsonRpcError.methodNotFound("Tool not found: " + toolName)
        );
    }
}
