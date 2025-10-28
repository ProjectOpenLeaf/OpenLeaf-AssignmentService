package org.example.business;

public interface CheckAuthorization {
    boolean isAuthorized(String therapistKeycloakId, String patientKeycloakId);
}
