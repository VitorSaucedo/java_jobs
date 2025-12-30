package com.vitorsaucedo.buscadorjava.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record LocationDTO(
        @JsonProperty("display_name") String displayName,
        @JsonProperty("area") List<String> areas
) {}
