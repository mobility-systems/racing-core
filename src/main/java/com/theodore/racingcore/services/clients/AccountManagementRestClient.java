package com.theodore.racingcore.services.clients;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Service
public class AccountManagementRestClient {

    private final RestClient client;

    public AccountManagementRestClient(RestClient client) {
        this.client = client;
    }

    public String fetchUserEmail(String userId) {
        return client.get()
                .uri("/misc/driver-email/{userId}", userId)
                .attributes(clientRegistrationId("mobility-api"))
                .retrieve()
                .body(String.class);
    }

}
