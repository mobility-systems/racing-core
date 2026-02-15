package com.theodore.racingcore.services;

import com.theodore.infrastructure.common.entities.modeltypes.RoleType;
import com.theodore.infrastructure.common.exceptions.AlreadyExistsException;
import com.theodore.infrastructure.common.exceptions.InvalidTokenException;
import com.theodore.racingcore.entities.racing.Driver;
import com.theodore.racingcore.repositories.DriverRepository;
import com.theodore.racingcore.services.clients.AccountManagementRestClient;
import com.theodore.racingcore.services.clients.AuthServerGrpcClient;
import com.theodore.racingcore.utils.RacingCoreTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class RacingServiceTest {

    @Mock
    private AccountManagementRestClient accountManagementRestClient;
    @Mock
    private DriverRepository driverRepository;
    @Mock
    private AuthServerGrpcClient authServerGrpcClient;

    @InjectMocks
    private RacingServiceImpl racingService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    class CreateNewDriver {

        private static final String ALIAS = "alias1";
        private static final String USERNAME = "alias1@mobilityapp.com";
        private static final String PASS = "password";
        private static final String USER_ID = "01KDBG51NE8EBT4B32NS0GKV5C";
        private static final String INVALID_TOKEN_MSG = "Invalid or empty token";
        private static final String DRIVER_ALREADY_EXISTS_MSG = "Driver with alias " + ALIAS + " already exists";

        @DisplayName("registerNewSimpleUser: User is not saved successfully and a compensation is triggered (negative scenario)")
        @Test
        void givenAuthIsNotJwt_whenCreateNewDriver_throwInvalidToken() {
            RacingCoreTestUtils.setAuthentication(new UsernamePasswordAuthenticationToken(USERNAME, PASS));

            assertThatThrownBy(() -> racingService.createNewDriver(ALIAS))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage(INVALID_TOKEN_MSG);

            then(accountManagementRestClient).shouldHaveNoInteractions();
            then(driverRepository).shouldHaveNoInteractions();
            then(authServerGrpcClient).shouldHaveNoInteractions();
        }

        @Test
        void givenJwtAuthIsNotAuthenticated_whenCreateNewDriver_throwInvalidToken() {
            JwtAuthenticationToken auth = mock(JwtAuthenticationToken.class);
            when(auth.isAuthenticated()).thenReturn(false);
            RacingCoreTestUtils.setAuthentication(auth);

            assertThatThrownBy(() -> racingService.createNewDriver(ALIAS))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage(INVALID_TOKEN_MSG);

            then(accountManagementRestClient).shouldHaveNoInteractions();
            then(driverRepository).shouldHaveNoInteractions();
            then(authServerGrpcClient).shouldHaveNoInteractions();
        }

        @Test
        void givenJwtTokenIsNull_whenCreateNewDriver_throwInvalidToken() {
            JwtAuthenticationToken auth = mock(JwtAuthenticationToken.class);
            given(auth.isAuthenticated()).willReturn(true);
            given(auth.getToken()).willReturn(null);
            RacingCoreTestUtils.setAuthentication(auth);

            assertThatThrownBy(() -> racingService.createNewDriver(ALIAS))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage(INVALID_TOKEN_MSG);

            then(accountManagementRestClient).shouldHaveNoInteractions();
            then(driverRepository).shouldHaveNoInteractions();
            then(authServerGrpcClient).shouldHaveNoInteractions();
        }

        @Test
        void givenDriverAlreadyExists_whenCreateNewDriver_throwAlreadyExists() {
            RacingCoreTestUtils.setAuthentication(RacingCoreTestUtils.jwtAuthWithUsername(USERNAME));

            given(accountManagementRestClient.fetchUserId(USERNAME)).willReturn(USER_ID);
            given(driverRepository.existsByIdOrAliasIgnoreCase(USER_ID, ALIAS)).willReturn(true);

            assertThatThrownBy(() -> racingService.createNewDriver(ALIAS))
                    .isInstanceOf(AlreadyExistsException.class)
                    .hasMessage(DRIVER_ALREADY_EXISTS_MSG);

            then(driverRepository).should(never()).save(any());
            then(authServerGrpcClient).shouldHaveNoInteractions();
        }

        @Test
        void givenValidAliasAndHasAuth_whenCreateNewDriver_saveDriverAndReturnUserId() {
            RacingCoreTestUtils.setAuthentication(RacingCoreTestUtils.jwtAuthWithUsername(USERNAME));

            given(accountManagementRestClient.fetchUserId(USERNAME)).willReturn(USER_ID);
            given(driverRepository.existsByIdOrAliasIgnoreCase(USER_ID, ALIAS)).willReturn(false);

            String result = racingService.createNewDriver(ALIAS);

            assertThat(result).isEqualTo(USER_ID);

            ArgumentCaptor<Driver> driverCaptor = ArgumentCaptor.forClass(Driver.class);
            then(driverRepository).should().save(driverCaptor.capture());

            Driver saved = driverCaptor.getValue();
            assertThat(saved.getId()).isEqualTo(USER_ID);
            assertThat(saved.getAlias()).isEqualTo(ALIAS);

            then(authServerGrpcClient).should().addUserRoleInAuthServer(USER_ID, RoleType.DRIVER);

            then(accountManagementRestClient).should().fetchUserId(USERNAME);
            then(driverRepository).should().existsByIdOrAliasIgnoreCase(USER_ID, ALIAS);
            then(driverRepository).should().save(any(Driver.class));
            then(authServerGrpcClient).should().addUserRoleInAuthServer(USER_ID, RoleType.DRIVER);
            then(accountManagementRestClient).shouldHaveNoMoreInteractions();
            then(driverRepository).shouldHaveNoMoreInteractions();
            then(authServerGrpcClient).shouldHaveNoMoreInteractions();
        }

    }


}
