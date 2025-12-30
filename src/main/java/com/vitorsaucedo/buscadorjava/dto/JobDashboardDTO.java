package com.vitorsaucedo.buscadorjava.dto;

import java.time.LocalDateTime;
import java.util.List;

public record JobDashboardDTO(
        List<JobResponseDTO> jobs,
        LocalDateTime lastSync
) {}
