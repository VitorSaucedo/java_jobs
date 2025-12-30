package com.vitorsaucedo.buscadorjava.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AdzunaResponseDTO(
        @JsonProperty("results") List<JobDTO> results,
        @JsonProperty("count") Long totalJobs
) {}
