package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {
    private Long id;
    private String patientKeycloakId;
    private String therapistKeycloakId;
    private LocalDateTime assignedAt;
    private String assignedBy;
    private Boolean active;
    private String notes;
}
