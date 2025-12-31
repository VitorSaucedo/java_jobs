package com.vitorsaucedo.buscadorjava.repository;

import com.vitorsaucedo.buscadorjava.model.JobListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface JobListingRepository extends JpaRepository<JobListing, Long> {
    Optional<JobListing> findByExternalId(String externalId);

    @Transactional
    void deleteByCreatedAtBefore(LocalDateTime thresholdDate);
}
