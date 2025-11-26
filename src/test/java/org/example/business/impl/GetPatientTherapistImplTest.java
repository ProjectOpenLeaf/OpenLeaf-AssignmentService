package org.example.business.impl;

import org.example.domain.Assignment;
import org.example.persistance.AssignmentRepository;
import org.example.persistance.entity.AssignmentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetPatientTherapistImplTest {

    private AssignmentRepository assignmentRepository;
    private GetPatientTherapistImpl getPatientTherapist;

    @BeforeEach
    void setUp() {
        assignmentRepository = mock(AssignmentRepository.class);
        getPatientTherapist = new GetPatientTherapistImpl(assignmentRepository);
    }

    @Test
    void getTherapist_ShouldReturnAssignment_WhenActiveAssignmentExists() {
        // Arrange
        String patientId = "patient123";

        AssignmentEntity entity = AssignmentEntity.builder()
                .id(10L)
                .patientKeycloakId(patientId)
                .therapistKeycloakId("therapistABC")
                .assignedAt(LocalDateTime.now())
                .assignedBy("admin")
                .active(true)
                .notes("Important note")
                .build();

        when(assignmentRepository.findByPatientKeycloakIdAndActiveTrue(patientId))
                .thenReturn(Optional.of(entity));

        // Act
        Assignment result = getPatientTherapist.getTherapist(patientId);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(patientId, result.getPatientKeycloakId());
        assertEquals("therapistABC", result.getTherapistKeycloakId());
        assertEquals("admin", result.getAssignedBy());
        assertEquals("Important note", result.getNotes());
        assertTrue(result.getActive());
    }

    @Test
    void getTherapist_ShouldThrowException_WhenNoActiveAssignmentExists() {
        // Arrange
        String patientId = "patient123";

        when(assignmentRepository.findByPatientKeycloakIdAndActiveTrue(patientId))
                .thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> getPatientTherapist.getTherapist(patientId));

        assertEquals("No therapist assigned to this patient", ex.getMessage());
    }

    @Test
    void getTherapist_ShouldCallRepositoryWithCorrectArgument() {
        // Arrange
        String patientId = "p1";

        when(assignmentRepository.findByPatientKeycloakIdAndActiveTrue(patientId))
                .thenReturn(Optional.of(
                        AssignmentEntity.builder()
                                .id(1L)
                                .patientKeycloakId(patientId)
                                .therapistKeycloakId("t1")
                                .assignedAt(LocalDateTime.now())
                                .assignedBy("system")
                                .active(true)
                                .notes("n")
                                .build()
                ));

        // Act
        getPatientTherapist.getTherapist(patientId);

        // Assert
        verify(assignmentRepository, times(1))
                .findByPatientKeycloakIdAndActiveTrue(patientId);
    }
}
