package com.vitorsaucedo.buscadorjava.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CompanyDTO(
        @JsonProperty("display_name") String name
) {}
