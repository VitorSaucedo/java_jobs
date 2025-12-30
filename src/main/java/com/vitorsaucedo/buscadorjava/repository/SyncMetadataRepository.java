package com.vitorsaucedo.buscadorjava.repository;

import com.vitorsaucedo.buscadorjava.model.SyncMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncMetadataRepository extends JpaRepository<SyncMetadata, String> {
}
