package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Builder(toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {

    private String exception;

    private String message;

    private Map<String,String> messages;

    private Integer status;

    private LocalDateTime timestamp;

}
