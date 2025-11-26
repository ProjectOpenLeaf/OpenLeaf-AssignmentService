package org.example.business.impl;

import org.example.domain.Assignment;
import org.example.persistance.AssignmentRepository;
import org.example.persistance.entity.AssignmentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssignTherapistImplTest {

    private AssignmentRepository assignmentRepository;
    private AssignTherapistImpl assignTherapist;

    @BeforeEach
    void setUp() {
        assignmentRepository = mock(AssignmentRepository.class);
        assignTherapist = new AssignTherapistImpl(assignmentRepository);
    }

    @Test
    void assign_ShouldCreateNewAssignment_WhenNoExistingActiveAssignment() {
        // Arrange
        String patientId = "patient123";
        String therapistId = "therapistABC";
        String assignedBy = "adminUser";
        String notes = "Initial assignment";

        when(assignmentRepository
                .existsByPatientKeycloakIdAndTherapistKeycloakIdAndActiveTrue(patientId, therapistId))
                .thenReturn(false);

        AssignmentEntity savedEntity = AssignmentEntity.builder()
                .id(1L)
                .patientKeycloakId(patientId)
                .therapistKeycloakId(therapistId)
                .assignedBy(assignedBy)
                .assignedAt(LocalDateTime.now())
                .active(true)
                .notes(notes)
                .build();

        when(assignmentRepository.save(any(AssignmentEntity.class)))
                .thenReturn(savedEntity);

        // Act
        Assignment result = assignTherapist.assign(patientId, therapistId, assignedBy, notes);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(patientId, result.getPatientKeycloakId());
        assertEquals(therapistId, result.getTherapistKeycloakId());
        assertEquals(assignedBy, result.getAssignedBy());
        assertEquals(notes, result.getNotes());
        assertTrue(result.getActive());

        // Verify save() was called with expected data
        ArgumentCaptor<AssignmentEntity> captor = ArgumentCaptor.forClass(AssignmentEntity.class);
        verify(assignmentRepository).save(captor.capture());

        AssignmentEntity passedToSave = captor.getValue();
        assertEquals(patientId, passedToSave.getPatientKeycloakId());
        assertEquals(therapistId, passedToSave.getTherapistKeycloakId());
        assertEquals(assignedBy, passedToSave.getAssignedBy());
        assertEquals(notes, passedToSave.getNotes());
        assertTrue(passedToSave.getActive());
    }

    @Test
    void assign_ShouldThrowException_WhenAssignmentAlreadyExists() {
        // Arrange
        String patientId = "patient123";
        String therapistId = "therapistABC";

        when(assignmentRepository
                .existsByPatientKeycloakIdAndTherapistKeycloakIdAndActiveTrue(patientId, therapistId))
                .thenReturn(true);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                assignTherapist.assign(patientId, therapistId, "adminUser", "notes"));

        assertEquals("Assignment already exists", ex.getMessage());

        verify(assignmentRepository, never()).save(any());
    }

    @Test
    void assign_ShouldCallRepositoryWithCorrectValues() {
        // Arrange
        String patientId = "p1";
        String therapistId = "t1";

        when(assignmentRepository
                .existsByPatientKeycloakIdAndTherapistKeycloakIdAndActiveTrue(patientId, therapistId))
                .thenReturn(false);

        AssignmentEntity savedEntity = AssignmentEntity.builder()
                .id(5L)
                .patientKeycloakId(patientId)
                .therapistKeycloakId(therapistId)
                .assignedBy("system")
                .assignedAt(LocalDateTime.now())
                .active(true)
                .notes("test")
                .build();

        when(assignmentRepository.save(any(AssignmentEntity.class)))
                .thenReturn(savedEntity);

        // Act
        assignTherapist.assign(patientId, therapistId, "system", "test");

        // Assert
        ArgumentCaptor<AssignmentEntity> captor = ArgumentCaptor.forClass(AssignmentEntity.class);
        verify(assignmentRepository).save(captor.capture());

        AssignmentEntity passed = captor.getValue();
        assertEquals(patientId, passed.getPatientKeycloakId());
        assertEquals(therapistId, passed.getTherapistKeycloakId());
        assertEquals("system", passed.getAssignedBy());
        assertEquals("test", passed.getNotes());
        assertTrue(passed.getActive());
        assertNotNull(passed.getAssignedAt());
    }
}
