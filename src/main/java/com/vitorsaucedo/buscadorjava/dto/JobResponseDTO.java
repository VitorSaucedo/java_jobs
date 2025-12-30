package com.vitorsaucedo.buscadorjava.dto;

import com.vitorsaucedo.buscadorjava.model.JobType;
import java.time.LocalDateTime;

public record JobResponseDTO(
        String title,
        String companyName,
        String locationName,
        String url,
        String description,
        String salaryRange,
        String technologies,
        String contractType,
        JobType jobType,
        LocalDateTime createdAt,
        String countryCode
) {}
