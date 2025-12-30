package com.vitorsaucedo.buscadorjava.controller;

import com.vitorsaucedo.buscadorjava.dto.JobDashboardDTO;
import com.vitorsaucedo.buscadorjava.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping
    public ResponseEntity<JobDashboardDTO> getDashboard() {
        return ResponseEntity.ok(jobService.getDashboardData());
    }

    @PostMapping("/fetch")
    public ResponseEntity<JobDashboardDTO> fetch() {
        return ResponseEntity.ok(jobService.fetchAndSaveJavaJobs());
    }
}

