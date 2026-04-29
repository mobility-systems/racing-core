package com.theodore.racingcore.integration;

import com.theodore.queue.common.services.MessagingService;
import com.theodore.racingcore.models.racing.requests.CreateNewDriverRequestDto;
import com.theodore.racingcore.repositories.DriverRepository;
import com.theodore.racingcore.services.clients.AccountManagementRestClient;
import com.theodore.racingcore.services.clients.AuthServerGrpcClient;
import com.theodore.racingcore.utils.RacingCoreTestConfigs;
import com.theodore.racingcore.utils.TestData;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Import(RacingCoreTestConfigs.class)
class RacingCoreApplicationIT extends BasePostgresTest {

    @Autowired
    TestDataFeeder testDataFeeder;

    @MockitoBean
    AccountManagementRestClient accountManagementRestClient;
    @MockitoBean
    AuthServerGrpcClient authServerGrpcClient;
    @MockitoBean
    MessagingService messagingService;

    @MockitoSpyBean
    DriverRepository driverRepository;

    WebTestClient client;

    @BeforeAll
    void initClient() {
        client = WebTestClient.bindToServer().baseUrl(baseUrl()).build();
        reset(accountManagementRestClient, driverRepository, authServerGrpcClient);
    }

    @BeforeEach
    void feedUserProfile() {
        testDataFeeder.feedDriverTable();
    }

    @Nested
    class CreateNewDriverTests {

        private static final String USER_EMAIL = TestData.ACTIVE_USER;
        private static final CreateNewDriverRequestDto EMPTY_ALIAS = new CreateNewDriverRequestDto("");
        private static final CreateNewDriverRequestDto EXISTING_ALIAS = new CreateNewDriverRequestDto(TestData.EXISTING_ALIAS_1);
        private static final CreateNewDriverRequestDto ALIAS = new CreateNewDriverRequestDto("driver1");


        @AfterEach
        void cleanUp() {
            testDataFeeder.cleanDriverTable();
        }

        @Test
        @DisplayName("createNewDriver: given empty alias when creating a new driver profile returns Bad Request (negative scenario)")
        void givenEmptyAlias_whenCreatingNewDriver_thenThrowBadRequest() {
            //given
            long countBefore = driverRepository.count();

            // when
            client.post()
                    .uri("/racing/driver/create")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer test-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(EMPTY_ALIAS)
                    .exchange()
                    .expectStatus().isBadRequest();

            // then
            verifyNoInteractions(accountManagementRestClient);
            verifyNoInteractions(authServerGrpcClient);

            long countAfter = driverRepository.count();
            assertThat(countAfter).isEqualTo(countBefore);
        }

        @Test
        @DisplayName("createNewDriver: given already existing alias when creating a new driver profile returns Bad Request (negative scenario)")
        void givenExistingAlias_whenCreatingNewDriver_thenThrowBadRequest() {
            //given
            long countBefore = driverRepository.count();

            when(accountManagementRestClient.fetchUserEmail(any(String.class))).thenReturn(USER_EMAIL);

            // when
            client.post()
                    .uri("/racing/driver/create")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer test-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(EXISTING_ALIAS)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.CONFLICT);

            // then
            verify(accountManagementRestClient, never()).fetchUserEmail(any(String.class));
            verifyNoInteractions(authServerGrpcClient);

            long countAfter = driverRepository.count();
            assertThat(countAfter).isEqualTo(countBefore);
        }

        @Test
        @DisplayName("createNewDriver: given a not existing alias when creating a new driver profile a driver is registered and returns Created (positive scenario)")
        void givenAcceptableAlias_whenCreatingNewDriver_thenReturnCreated() {
            //given
            long countBefore = driverRepository.count();

            when(accountManagementRestClient.fetchUserEmail(any(String.class))).thenReturn(USER_EMAIL);

            // when
            client.post()
                    .uri("/racing/driver/create")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer test-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(ALIAS)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.CREATED);

            // then
            verify(accountManagementRestClient, times(1)).fetchUserEmail(any(String.class));
            verify(authServerGrpcClient, times(1)).addUserRoleInAuthServer(any(), any());

            long countAfter = driverRepository.count();
            assertThat(countAfter).isGreaterThan(countBefore);
        }

    }
}
