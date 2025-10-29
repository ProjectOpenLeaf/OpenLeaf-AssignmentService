package org.example.business;

import org.example.domain.Assignment;

public interface GetPatientTherapist {
    Assignment getTherapist(String patientKeycloakId);
}
