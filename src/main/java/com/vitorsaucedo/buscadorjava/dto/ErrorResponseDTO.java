package com.vitorsaucedo.buscadorjava.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        int status,
        String message,
        LocalDateTime timestamp,
        String path
) {}
