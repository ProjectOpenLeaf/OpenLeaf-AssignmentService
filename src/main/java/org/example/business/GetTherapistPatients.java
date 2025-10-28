package org.example.business;

import org.example.domain.Assignment;

import java.util.List;

public interface GetTherapistPatients {
    List<Assignment> getPatients(String therapistKeycloakId);
}