package org.example.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignRequest {
    private String patientKeycloakId;
    private String therapistKeycloakId;
    private String notes; // Optional admin notes
}
