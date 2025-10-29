package org.example.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.business.dto.AccountDeletionEvent;
import org.example.config.RabbitMQConfig;
import org.example.persistance.AssignmentRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Consumer for account deletion events in Assignment Service
 * Deletes all assignments related to the deleted user
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AssignmentDeletionConsumer {

    private final AssignmentRepository assignmentRepository;

    /**
     * Listen for account deletion events and delete related assignments
     *
     * @param event The account deletion event
     */
    @RabbitListener(queues = RabbitMQConfig.ASSIGNMENT_DELETION_QUEUE)
    @Transactional
    public void handleAccountDeletion(AccountDeletionEvent event) {
        try {
            String userKeycloakId = event.getUserKeycloakId();
            log.info("Received account deletion event for user: {} - Reason: {}",
                    userKeycloakId, event.getReason());

            // Delete assignments where user is the patient
            int deletedAsPatient = assignmentRepository.deleteByPatientKeycloakId(userKeycloakId);
            log.info("Deleted {} assignments where user was patient", deletedAsPatient);

            // Delete assignments where user is the therapist
            int deletedAsTherapist = assignmentRepository.deleteByTherapistKeycloakId(userKeycloakId);
            log.info("Deleted {} assignments where user was therapist", deletedAsTherapist);

            log.info("Successfully processed account deletion for user: {} in Assignment Service",
                    userKeycloakId);

        } catch (Exception e) {
            log.error("Failed to process account deletion event for user: {}",
                    event.getUserKeycloakId(), e);
            // In production, you might want to implement a dead letter queue
            // or retry mechanism here
            throw e; // This will trigger RabbitMQ redelivery
        }
    }
}






