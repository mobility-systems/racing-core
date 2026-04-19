package com.theodore.racingcore.services.saga;

import com.theodore.infrastructure.common.entities.enums.RoleType;

import java.util.Set;

public interface SagaCompensationActionService {

    void authServerRolesRollback(String userId, Set<RoleType> rolesToBeRemoved, String logMsg);

}
