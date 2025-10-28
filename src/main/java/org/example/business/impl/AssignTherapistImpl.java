package org.example.business.impl;

import lombok.RequiredArgsConstructor;
import org.example.business.AssignTherapist;
import org.example.domain.Assignment;
import org.example.persistance.AssignmentRepository;
import org.example.persistance.entity.AssignmentEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AssignTherapistImpl implements AssignTherapist {

    private final AssignmentRepository assignmentRepository;

    @Override
    public Assignment assign(String patientKeycloakId, String therapistKeycloakId,
                             String assignedBy, String notes) {

        // Check if assignment already exists
        if (assignmentRepository.existsByPatientKeycloakIdAndTherapistKeycloakIdAndActiveTrue(
                patientKeycloakId, therapistKeycloakId)) {
            throw new RuntimeException("Assignment already exists");
        }

        AssignmentEntity entity = AssignmentEntity.builder()
                .patientKeycloakId(patientKeycloakId)
                .therapistKeycloakId(therapistKeycloakId)
                .assignedBy(assignedBy)
                .assignedAt(LocalDateTime.now())
                .active(true)
                .notes(notes)
                .build();

        AssignmentEntity saved = assignmentRepository.save(entity);
        return toAssignment(saved);
    }

    private Assignment toAssignment(AssignmentEntity entity) {
        return Assignment.builder()
                .id(entity.getId())
                .patientKeycloakId(entity.getPatientKeycloakId())
                .therapistKeycloakId(entity.getTherapistKeycloakId())
                .assignedAt(entity.getAssignedAt())
                .assignedBy(entity.getAssignedBy())
                .active(entity.getActive())
                .notes(entity.getNotes())
                .build();
    }
}
