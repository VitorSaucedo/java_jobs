package com.vitorsaucedo.buscadorjava.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String externalId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String companyName;
    private String locationName;
    private String countryCode;
    private String url;

    private Double salaryMin;
    private Double salaryMax;
    private String contractType;
    private String technologies;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private LocalDateTime createdAt;
    private LocalDateTime fetchedAt;

    @PrePersist
    protected void onCreate() {
        this.fetchedAt = LocalDateTime.now();
    }
}
