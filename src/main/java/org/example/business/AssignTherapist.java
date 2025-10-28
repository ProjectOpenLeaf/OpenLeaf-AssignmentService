package org.example.business;

import org.example.domain.Assignment;

public interface AssignTherapist {
    Assignment assign(String patientKeycloakId, String therapistKeycloakId,
                      String assignedBy, String notes);
}
