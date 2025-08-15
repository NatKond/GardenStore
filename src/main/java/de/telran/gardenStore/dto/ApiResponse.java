package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.telran.gardenStore.serializer.SensitiveData;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Builder(toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private String exception;
    @SensitiveData
    private String message;
    @SensitiveData
    private Map<String, String> messages;
    private Integer status;
    private LocalDateTime timestamp;

    public static ApiResponse error(String message, Integer status) {
        return ApiResponse.builder()
                .message(message)
                .status(status)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse error(Exception exception, Integer status) {
        return ApiResponse.builder()
                .exception(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .status(status)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse error(Exception exception, Map<String, String> messages, Integer status) {
        return ApiResponse.builder()
                .exception(exception.getClass().getSimpleName())
                .messages(messages)
                .status(status)
                .timestamp(LocalDateTime.now())
                .build();
    }
}