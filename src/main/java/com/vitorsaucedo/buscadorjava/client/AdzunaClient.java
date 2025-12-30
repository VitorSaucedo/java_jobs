package com.vitorsaucedo.buscadorjava.client;

import com.vitorsaucedo.buscadorjava.dto.AdzunaResponseDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/jobs")
public interface AdzunaClient {

    @GetExchange("/{country}/search/1")
    AdzunaResponseDTO getJavaJobs(
            @PathVariable String country,
            @RequestParam("app_id") String appId,
            @RequestParam("app_key") String appKey,
            @RequestParam("what") String keyword,
            @RequestParam("results_per_page") int count,
            @RequestParam("content-type") String contentType
    );
}