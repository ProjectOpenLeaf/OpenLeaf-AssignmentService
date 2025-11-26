package org.example.business.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.example.persistance.AssignmentRepository;
import org.example.persistance.entity.AssignmentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnassignTherapistImplTest {

    private AssignmentRepository assignmentRepository;
    private UnassignTherapistImpl unassignTherapist;

    @BeforeEach
    void setUp() {
        assignmentRepository = mock(AssignmentRepository.class);
        unassignTherapist = new UnassignTherapistImpl(assignmentRepository);
    }

    @Test
    void unassign_ShouldSetActiveFalse_AndSaveEntity_WhenFound() {
        // Arrange
        Long assignmentId = 10L;

        AssignmentEntity entity = AssignmentEntity.builder()
                .id(assignmentId)
                .active(true)
                .build();

        when(assignmentRepository.findById(assignmentId))
                .thenReturn(Optional.of(entity));

        // Act
        unassignTherapist.unassign(assignmentId);

        // Assert
        assertFalse(entity.getActive(), "Entity should be marked inactive");
        verify(assignmentRepository).save(entity);
    }

    @Test
    void unassign_ShouldThrowException_WhenAssignmentNotFound() {
        // Arrange
        Long assignmentId = 99L;

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> unassignTherapist.unassign(assignmentId));

        assertEquals("Assignment not found", ex.getMessage());
        verify(assignmentRepository, never()).save(any());
    }

    @Test
    void unassign_ShouldCallFindByIdWithCorrectArgument() {
        // Arrange
        Long assignmentId = 5L;

        when(assignmentRepository.findById(assignmentId))
                .thenReturn(Optional.of(
                        AssignmentEntity.builder()
                                .id(assignmentId)
                                .active(true)
                                .build()
                ));

        // Act
        unassignTherapist.unassign(assignmentId);

        // Assert
        verify(assignmentRepository, times(1)).findById(assignmentId);
    }
}
