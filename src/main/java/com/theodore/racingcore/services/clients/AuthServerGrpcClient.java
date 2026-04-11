package com.theodore.racingcore.services.clients;

import com.theodore.infrastructure.common.entities.enums.RoleType;
import com.theodore.user.AddRoleRequest;
import com.theodore.user.AuthServerRoleManagementGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthServerGrpcClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServerGrpcClient.class);

    @GrpcClient("auth-server")
    AuthServerRoleManagementGrpc.AuthServerRoleManagementBlockingStub authServerRoleClient;

    /**
     * Sends a gRPC call to the auth server to add a role to a user.
     *
     * @param userId The user's id
     * @param role The role type that will be added to the user's roles
     */
    public void addUserRoleInAuthServer(String userId, RoleType role) {
        LOGGER.info("Sending request via grpc to auth server to add role: {} to user: {}", role, userId);
        var grpcRequest = AddRoleRequest.newBuilder()
                .setUserId(userId)
                .setRole(role.getScopeValue())
                .build();

        this.authServerRoleClient.addRole(grpcRequest);

        LOGGER.info("Adding Role was successful");
    }

}
