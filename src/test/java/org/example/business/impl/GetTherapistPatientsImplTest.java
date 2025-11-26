package org.example.business.impl;

import org.example.domain.Assignment;
import org.example.persistance.AssignmentRepository;
import org.example.persistance.entity.AssignmentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetTherapistPatientsImplTest {

    private AssignmentRepository assignmentRepository;
    private GetTherapistPatientsImpl getTherapistPatients;

    @BeforeEach
    void setUp() {
        assignmentRepository = mock(AssignmentRepository.class);
        getTherapistPatients = new GetTherapistPatientsImpl(assignmentRepository);
    }

    @Test
    void getPatients_ShouldReturnMappedAssignments_WhenEntitiesExist() {
        // Arrange
        String therapistId = "therapist123";

        AssignmentEntity e1 = AssignmentEntity.builder()
                .id(1L)
                .patientKeycloakId("patientA")
                .therapistKeycloakId(therapistId)
                .assignedAt(LocalDateTime.now())
                .assignedBy("admin1")
                .active(true)
                .notes("note1")
                .build();

        AssignmentEntity e2 = AssignmentEntity.builder()
                .id(2L)
                .patientKeycloakId("patientB")
                .therapistKeycloakId(therapistId)
                .assignedAt(LocalDateTime.now())
                .assignedBy("admin2")
                .active(true)
                .notes("note2")
                .build();

        when(assignmentRepository.findByTherapistKeycloakIdAndActiveTrue(therapistId))
                .thenReturn(List.of(e1, e2));

        // Act
        List<Assignment> result = getTherapistPatients.getPatients(therapistId);

        // Assert
        assertEquals(2, result.size());

        Assignment a1 = result.get(0);
        Assignment a2 = result.get(1);

        assertEquals(e1.getId(), a1.getId());
        assertEquals(e1.getPatientKeycloakId(), a1.getPatientKeycloakId());
        assertEquals(e1.getNotes(), a1.getNotes());

        assertEquals(e2.getId(), a2.getId());
        assertEquals(e2.getPatientKeycloakId(), a2.getPatientKeycloakId());
        assertEquals(e2.getNotes(), a2.getNotes());
    }

    @Test
    void getPatients_ShouldReturnEmptyList_WhenNoAssignmentsFound() {
        // Arrange
        String therapistId = "therapist123";

        when(assignmentRepository.findByTherapistKeycloakIdAndActiveTrue(therapistId))
                .thenReturn(List.of());

        // Act
        List<Assignment> result = getTherapistPatients.getPatients(therapistId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getPatients_ShouldCallRepositoryWithCorrectArgument() {
        // Arrange
        String therapistId = "therapistX";

        when(assignmentRepository.findByTherapistKeycloakIdAndActiveTrue(therapistId))
                .thenReturn(List.of());

        // Act
        getTherapistPatients.getPatients(therapistId);

        // Assert
        verify(assignmentRepository, times(1))
                .findByTherapistKeycloakIdAndActiveTrue(therapistId);
    }
}
