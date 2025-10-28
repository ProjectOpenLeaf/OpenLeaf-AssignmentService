package org.example.business.impl;

import lombok.RequiredArgsConstructor;
import org.example.business.GetTherapistPatients;
import org.example.domain.Assignment;
import org.example.persistance.AssignmentRepository;
import org.example.persistance.entity.AssignmentEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetTherapistPatientsImpl implements GetTherapistPatients {

    private final AssignmentRepository assignmentRepository;

    @Override
    public List<Assignment> getPatients(String therapistKeycloakId) {
        List<AssignmentEntity> entities = assignmentRepository
                .findByTherapistKeycloakIdAndActiveTrue(therapistKeycloakId);

        return entities.stream()
                .map(this::toAssignment)
                .collect(Collectors.toList());
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
