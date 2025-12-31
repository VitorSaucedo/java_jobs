package com.vitorsaucedo.buscadorjava.service;

import com.vitorsaucedo.buscadorjava.client.AdzunaClient;
import com.vitorsaucedo.buscadorjava.dto.AdzunaResponseDTO;
import com.vitorsaucedo.buscadorjava.dto.JobDTO;
import com.vitorsaucedo.buscadorjava.dto.JobDashboardDTO;
import com.vitorsaucedo.buscadorjava.dto.JobResponseDTO;
import com.vitorsaucedo.buscadorjava.mapper.JobMapper;
import com.vitorsaucedo.buscadorjava.model.JobListing;
import com.vitorsaucedo.buscadorjava.model.SyncMetadata;
import com.vitorsaucedo.buscadorjava.repository.JobListingRepository;
import com.vitorsaucedo.buscadorjava.repository.SyncMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final AdzunaClient adzunaClient;
    private final JobListingRepository repository;
    private final SyncMetadataRepository syncRepository; // Injetado
    private final JobMapper jobMapper;

    @Value("${adzuna.api.id}")
    private String appId;

    @Value("${adzuna.api.key}")
    private String appKey;

    public JobDashboardDTO getDashboardData() {
        List<JobResponseDTO> jobs = repository.findAll().stream()
                .map(jobMapper::toResponseDTO)
                .toList();

        LocalDateTime lastSync = syncRepository.findById("LAST_SYNC")
                .map(SyncMetadata::getLastSyncTimestamp)
                .orElse(null);

        return new JobDashboardDTO(jobs, lastSync);
    }

    @Transactional
    public JobDashboardDTO fetchAndSaveJavaJobs() {
        List<String> countries = List.of("br", "us", "gb");

        countries.forEach(country -> {
            try {
                String keyword = country.equalsIgnoreCase("br") ? "Java" : "Java Remote";
                AdzunaResponseDTO response = adzunaClient.getJavaJobs(country, appId, appKey, keyword, 15, "application/json");

                if (response != null && response.results() != null) {
                    processResponse(response.results(), country);
                }
            } catch (Exception e) {
                System.err.println("Erro ao sincronizar " + country + ": " + e.getMessage());
            }
        });

        SyncMetadata metadata = SyncMetadata.builder()
                .id("LAST_SYNC")
                .lastSyncTimestamp(LocalDateTime.now())
                .build();
        syncRepository.save(metadata);

        return getDashboardData();
    }

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void performDatabaseMaintenance() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        repository.deleteByCreatedAtBefore(oneMonthAgo);
        System.out.println("Manutenção concluída: Vagas com mais de 1 mês removidas.");
    }

    private void processResponse(List<JobDTO> results, String country) {
        List<JobListing> listings = results.stream()
                .map(dto -> jobMapper.toEntity(dto, country))
                .toList();

        listings.forEach(job -> {
            repository.findByExternalId(job.getExternalId())
                    .ifPresentOrElse(
                            existing -> {
                                job.setId(existing.getId());
                                repository.save(job);
                            },
                            () -> repository.save(job)
                    );
        });
    }
}