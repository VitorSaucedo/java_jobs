package com.vitorsaucedo.buscadorjava.mapper;

import com.vitorsaucedo.buscadorjava.dto.JobDTO;
import com.vitorsaucedo.buscadorjava.dto.JobResponseDTO;
import com.vitorsaucedo.buscadorjava.model.JobListing;
import com.vitorsaucedo.buscadorjava.model.JobType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JobMapper {

    public JobListing toEntity(JobDTO dto, String country) {
        if (dto == null) return null;

        return JobListing.builder()
                .externalId(dto.id())
                .title(dto.title())
                .description(dto.description())
                .companyName(dto.company() != null ? dto.company().name() : "N/A")
                .locationName(dto.location() != null ? dto.location().displayName() : "N/A")
                .url(dto.redirectUrl())
                .salaryMin(dto.salaryMin())
                .salaryMax(dto.salaryMax())
                .countryCode(country)
                .technologies(extractTechs(dto.title() + " " + dto.description()))
                .jobType(identifyJobType(dto))
                .createdAt(LocalDateTime.now())
                .build();
    }

    public JobResponseDTO toResponseDTO(JobListing entity) {
        return new JobResponseDTO(
                entity.getTitle(),
                entity.getCompanyName(),
                entity.getLocationName(),
                entity.getUrl(),
                entity.getDescription(),
                formatSalary(entity.getSalaryMin(), entity.getSalaryMax(), entity.getDescription(), entity.getCountryCode()),
                entity.getTechnologies(),
                entity.getContractType(),
                entity.getJobType(),
                entity.getCreatedAt(),
                entity.getCountryCode()
        );
    }

    private String extractTechs(String text) {
        List<String> stack = List.of(
                "Spring Boot", "Spring Cloud", "Hibernate", "JPA", "Microservices",
                "PostgreSQL", "MySQL", "MongoDB", "NoSQL", "Redis", "RabbitMQ", "Kafka",
                "Docker", "Kubernetes", "AWS", "Azure", "GCP", "JUnit", "Mockito",
                "Maven", "Gradle", "Quarkus", "Micronaut", "REST API", "GraphQL",
                "React", "Angular", "TypeScript", "JavaScript", "HTML", "CSS"
        );

        String lowerText = text.toLowerCase();

        return stack.stream()
                .filter(tech -> lowerText.contains(tech.toLowerCase()))
                .distinct()
                .collect(Collectors.joining(", "));
    }

    private String formatSalary(Double min, Double max, String description, String country) {
        String symbol = switch (country.toLowerCase()) {
            case "br" -> "R$";
            case "us" -> "$";
            case "gb" -> "£";
            default -> "¤";
        };

        if (min != null && min > 0) {
            String formattedMin = String.format("%,.2f", min);
            if (max != null && max > 0 && !max.equals(min)) {
                return symbol + " " + formattedMin + " - " + String.format("%,.2f", max);
            }
            return symbol + " " + formattedMin;
        }

        if (description != null && (description.contains("R$") || description.contains("$") || description.contains("£"))) {
            return "Ver na descrição";
        }

        return "Salário a combinar";
    }

    private JobType identifyJobType(JobDTO dto) {
        String content = (dto.title() + " " + dto.description()).toLowerCase();

        boolean isRemote = content.contains("remoto") ||
                content.contains("remote") ||
                content.contains("home office") ||
                content.contains("anywhere") ||
                content.contains("teletrabalho");

        return isRemote ? JobType.REMOTE : JobType.ON_SITE;
    }
}