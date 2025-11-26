package org.example.business.impl;

import org.example.persistance.AssignmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckAuthorizationImplTest {

    private AssignmentRepository assignmentRepository;
    private CheckAuthorizationImpl checkAuthorization;

    @BeforeEach
    void setUp() {
        assignmentRepository = mock(AssignmentRepository.class);
        checkAuthorization = new CheckAuthorizationImpl(assignmentRepository);
    }

    @Test
    void isAuthorized_ShouldReturnTrue_WhenActiveAssignmentExists() {
        // Arrange
        String therapistId = "therapist123";
        String patientId = "patientABC";

        when(assignmentRepository
                .existsByPatientKeycloakIdAndTherapistKeycloakIdAndActiveTrue(patientId, therapistId))
                .thenReturn(true);

        // Act
        boolean result = checkAuthorization.isAuthorized(therapistId, patientId);

        // Assert
        assertTrue(result);
    }

    @Test
    void isAuthorized_ShouldReturnFalse_WhenNoActiveAssignmentExists() {
        // Arrange
        String therapistId = "therapist123";
        String patientId = "patientABC";

        when(assignmentRepository
                .existsByPatientKeycloakIdAndTherapistKeycloakIdAndActiveTrue(patientId, therapistId))
                .thenReturn(false);

        // Act
        boolean result = checkAuthorization.isAuthorized(therapistId, patientId);

        // Assert
        assertFalse(result);
    }

    @Test
    void isAuthorized_ShouldCallRepositoryWithCorrectArguments() {
        // Arrange
        String therapistId = "t1";
        String patientId = "p1";

        when(assignmentRepository
                .existsByPatientKeycloakIdAndTherapistKeycloakIdAndActiveTrue(patientId, therapistId))
                .thenReturn(true);

        // Act
        checkAuthorization.isAuthorized(therapistId, patientId);

        // Assert
        verify(assignmentRepository, times(1))
                .existsByPatientKeycloakIdAndTherapistKeycloakIdAndActiveTrue(patientId, therapistId);
    }
}
