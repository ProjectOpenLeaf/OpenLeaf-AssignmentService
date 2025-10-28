package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.business.AssignTherapist;
import org.example.business.CheckAuthorization;
import org.example.business.GetTherapistPatients;
import org.example.business.UnassignTherapist;
import org.example.business.dto.AssignRequest;
import org.example.business.dto.AssignResponse;
import org.example.business.dto.AuthorizationResponse;
import org.example.business.dto.PatientInfo;
import org.example.domain.Assignment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignTherapist assignTherapist;
    private final GetTherapistPatients getTherapistPatients;
    private final CheckAuthorization checkAuthorization;
    private final UnassignTherapist unassignTherapist;

    @PostMapping("/assign")
    public ResponseEntity<AssignResponse> assignTherapistToPatient(
            @RequestHeader("X-User-Id") String adminKeycloakId,
            @RequestBody AssignRequest request) {

        Assignment assignment = assignTherapist.assign(
                request.getPatientKeycloakId(),
                request.getTherapistKeycloakId(),
                adminKeycloakId,
                request.getNotes()
        );

        AssignResponse response = AssignResponse.builder()
                .id(assignment.getId())
                .patientKeycloakId(assignment.getPatientKeycloakId())
                .therapistKeycloakId(assignment.getTherapistKeycloakId())
                .assignedAt(assignment.getAssignedAt())
                .message("Therapist assigned successfully")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/therapist/{therapistId}/patients")
    public ResponseEntity<List<PatientInfo>> getTherapistPatients(
            @PathVariable String therapistId) {

        List<Assignment> assignments = getTherapistPatients.getPatients(therapistId);

        List<PatientInfo> patients = assignments.stream()
                .map(assignment -> PatientInfo.builder()
                        .assignmentId(assignment.getId())
                        .patientKeycloakId(assignment.getPatientKeycloakId())
                        .assignedAt(assignment.getAssignedAt())
                        .notes(assignment.getNotes())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(patients);
    }

    @GetMapping("/check")
    public ResponseEntity<AuthorizationResponse> checkAuthorization(
            @RequestParam String therapistId,
            @RequestParam String patientId) {

        boolean authorized = checkAuthorization.isAuthorized(therapistId, patientId);

        AuthorizationResponse response = AuthorizationResponse.builder()
                .authorized(authorized)
                .message(authorized ? "Access granted" : "Access denied")
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<String> unassignTherapist(
            @PathVariable Long assignmentId) {

        unassignTherapist.unassign(assignmentId);
        return ResponseEntity.ok("Therapist unassigned successfully");
    }
}
