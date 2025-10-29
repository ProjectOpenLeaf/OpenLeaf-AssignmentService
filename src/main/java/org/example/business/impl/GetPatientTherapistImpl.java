package org.example.business.impl;

import lombok.RequiredArgsConstructor;
import org.example.business.GetPatientTherapist;
import org.example.domain.Assignment;
import org.example.persistance.AssignmentRepository;
import org.example.persistance.entity.AssignmentEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPatientTherapistImpl implements GetPatientTherapist {

    private final AssignmentRepository assignmentRepository;

    @Override
    public Assignment getTherapist(String patientKeycloakId) {
        AssignmentEntity entity = assignmentRepository
                .findByPatientKeycloakIdAndActiveTrue(patientKeycloakId)
                .orElseThrow(() -> new RuntimeException("No therapist assigned to this patient"));

        return toAssignment(entity);
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
