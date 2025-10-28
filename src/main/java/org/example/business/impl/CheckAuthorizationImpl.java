package org.example.business.impl;

import lombok.RequiredArgsConstructor;
import org.example.business.CheckAuthorization;
import org.example.persistance.AssignmentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckAuthorizationImpl implements CheckAuthorization {

    private final AssignmentRepository assignmentRepository;

    @Override
    public boolean isAuthorized(String therapistKeycloakId, String patientKeycloakId) {
        return assignmentRepository.existsByPatientKeycloakIdAndTherapistKeycloakIdAndActiveTrue(
                patientKeycloakId, therapistKeycloakId);
    }
}
