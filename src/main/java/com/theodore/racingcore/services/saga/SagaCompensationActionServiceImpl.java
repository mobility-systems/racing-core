package com.theodore.racingcore.services.saga;

import com.theodore.infrastructure.common.entities.enums.RoleType;
import com.theodore.queue.common.authserver.RolesRollbackEventDto;
import com.theodore.queue.common.services.MessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SagaCompensationActionServiceImpl implements SagaCompensationActionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SagaCompensationActionServiceImpl.class);

    private final MessagingService messagingService;

    public SagaCompensationActionServiceImpl(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    /**
     * Rolls back the assignment of roles to a user
     *
     * @param userId           The id of the user
     * @param rolesToBeRemoved The roles that have to be removed from the user
     * @param logMsg           The process that failed
     */
    @Override
    public void authServerRolesRollback(String userId, Set<RoleType> rolesToBeRemoved, String logMsg) {
        LOGGER.info("{} process failed. Rolling back roles from auth server for user with id: {} ", logMsg, userId);
        var rollbackEvent = new RolesRollbackEventDto(userId, rolesToBeRemoved);
        messagingService.rollbackRolesAssignment(rollbackEvent);
    }

}
