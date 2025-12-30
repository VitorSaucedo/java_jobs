package com.vitorsaucedo.buscadorjava.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SyncMetadata {
    @Id
    private String id;
    private LocalDateTime lastSyncTimestamp;
}
