package org.example.business.impl;

import lombok.RequiredArgsConstructor;
import org.example.business.UnassignTherapist;
import org.example.persistance.AssignmentRepository;
import org.example.persistance.entity.AssignmentEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnassignTherapistImpl implements UnassignTherapist {

    private final AssignmentRepository assignmentRepository;

    @Override
    public void unassign(Long assignmentId) {
        AssignmentEntity entity = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Soft delete - set active to false
        entity.setActive(false);
        assignmentRepository.save(entity);
    }
}
