package org.example.examenspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ErrorResponseDTO {
    private LocalDateTime timestamp;
    Integer errorCode;
    private String error;
    private String message;
}