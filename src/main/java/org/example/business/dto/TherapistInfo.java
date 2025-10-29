package org.example.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TherapistInfo {
    private Long assignmentId;
    private String therapistKeycloakId;
    private LocalDateTime assignedAt;
    private String notes;
}