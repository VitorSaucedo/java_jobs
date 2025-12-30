package com.vitorsaucedo.buscadorjava.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JobDTO(
        String id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("redirect_url") String redirectUrl,
        @JsonProperty("company") CompanyDTO company,
        @JsonProperty("location") LocationDTO location,
        @JsonProperty("salary_min") Double salaryMin,
        @JsonProperty("salary_max") Double salaryMax,
        @JsonProperty("contract_type") String contractType,
        @JsonProperty("created") String createdAt
) {}
