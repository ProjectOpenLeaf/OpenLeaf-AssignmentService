package org.example.persistance;

import org.example.persistance.entity.AssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<AssignmentEntity, Long> {
    List<AssignmentEntity> findByTherapistKeycloakIdAndActiveTrue(String therapistKeycloakId);

    Optional<AssignmentEntity> findByPatientKeycloakIdAndActiveTrue(String patientKeycloakId);

    Optional<AssignmentEntity> findByPatientKeycloakIdAndTherapistKeycloakIdAndActiveTrue(
            String patientKeycloakId, String therapistKeycloakId);

    boolean existsByPatientKeycloakIdAndTherapistKeycloakIdAndActiveTrue(
            String patientKeycloakId, String therapistKeycloakId);
}
