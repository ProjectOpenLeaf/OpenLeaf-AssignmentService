package org.example.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "patient_therapist_assignments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"patient_keycloak_id", "therapist_keycloak_id"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_keycloak_id", nullable = false)
    private String patientKeycloakId;

    @Column(name = "therapist_keycloak_id", nullable = false)
    private String therapistKeycloakId;

    @Column(nullable = false)
    private LocalDateTime assignedAt;

    @Column(name = "assigned_by")
    private String assignedBy; // Keycloak ID of admin who made assignment

    @Column(nullable = false)
    private Boolean active = true;

    @Column(columnDefinition = "TEXT")
    private String notes; // Optional admin notes about the assignment
}
