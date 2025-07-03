package de.telran.gardenStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppErrorResponse {

    private String exception;

    private String message;

    private Map<String,String> messages;

    private Integer status;

    private LocalDateTime timestamp;

}
