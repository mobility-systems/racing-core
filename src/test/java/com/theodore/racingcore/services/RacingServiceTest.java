package com.theodore.racingcore.services;

import com.theodore.infrastructure.common.entities.modeltypes.RoleType;
import com.theodore.infrastructure.common.exceptions.AlreadyExistsException;
import com.theodore.infrastructure.common.exceptions.InvalidTokenException;
import com.theodore.racingcore.entities.racing.Driver;
import com.theodore.racingcore.repositories.DriverRepository;
import com.theodore.racingcore.services.clients.AccountManagementRestClient;
import com.theodore.racingcore.services.clients.AuthServerGrpcClient;
import com.theodore.racingcore.utils.RacingCoreTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import static org.assertj.core.api.Assertions.*;
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

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @DisplayName("registerNewSimpleUser: User is not saved successfully and a compensation is triggered (negative scenario)")
    @Test
    void givenAuthIsNotJwt_whenCreateNewDriver_throwInvalidToken() {
        RacingCoreTestUtils.setAuthentication(new UsernamePasswordAuthenticationToken("user", "pass"));

        assertThatThrownBy(() -> racingService.createNewDriver("alias1"))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Invalid or empty token");

        then(accountManagementRestClient).shouldHaveNoInteractions();
        then(driverRepository).shouldHaveNoInteractions();
        then(authServerGrpcClient).shouldHaveNoInteractions();
    }

    @Test
    void givenJwtAuthIsNotAuthenticated_whenCreateNewDriver_throwInvalidToken() {
        JwtAuthenticationToken auth = mock(JwtAuthenticationToken.class);
        when(auth.isAuthenticated()).thenReturn(false);
        RacingCoreTestUtils.setAuthentication(auth);

        assertThatThrownBy(() -> racingService.createNewDriver("alias1"))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Invalid or empty token");

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

        assertThatThrownBy(() -> racingService.createNewDriver("alias1"))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Invalid or empty token");

        then(accountManagementRestClient).shouldHaveNoInteractions();
        then(driverRepository).shouldHaveNoInteractions();
        then(authServerGrpcClient).shouldHaveNoInteractions();
    }

    //todo
    @Test
    void givenDriverAlreadyExists_whenCreateNewDriver_throwAlreadyExists() {
        RacingCoreTestUtils.setAuthentication(RacingCoreTestUtils.jwtAuthWithUsername("john"));

        given(accountManagementRestClient.fetchUserId("john")).willReturn("user-123");
        given(driverRepository.existsByIdOrAliasIgnoreCase("user-123", "Max")).willReturn(true);

        assertThatThrownBy(() -> racingService.createNewDriver("Max"))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessage("Driver with alias Max already exists");

        then(driverRepository).should(never()).save(any());
        then(authServerGrpcClient).shouldHaveNoInteractions();
    }

    @Test
    void givenValidAliasAndHasAuth_whenCreateNewDriver_saveDriverAndReturnUserId() {
        RacingCoreTestUtils.setAuthentication(RacingCoreTestUtils.jwtAuthWithUsername("john"));

        given(accountManagementRestClient.fetchUserId("john")).willReturn("user-123");
        given(driverRepository.existsByIdOrAliasIgnoreCase("user-123", "Max")).willReturn(false);

        String result = racingService.createNewDriver("Max");

        assertThat(result).isEqualTo("user-123");

        ArgumentCaptor<Driver> driverCaptor = ArgumentCaptor.forClass(Driver.class);
        then(driverRepository).should().save(driverCaptor.capture());

        Driver saved = driverCaptor.getValue();
        assertThat(saved.getId()).isEqualTo("user-123");
        assertThat(saved.getAlias()).isEqualTo("Max");

        then(authServerGrpcClient).should().addUserRoleInAuthServer("user-123", RoleType.DRIVER);

        then(accountManagementRestClient).should().fetchUserId("john");
        then(driverRepository).should().existsByIdOrAliasIgnoreCase("user-123", "Max");
        then(driverRepository).should().save(any(Driver.class));
        then(authServerGrpcClient).should().addUserRoleInAuthServer("user-123", RoleType.DRIVER);
        then(accountManagementRestClient).shouldHaveNoMoreInteractions();
        then(driverRepository).shouldHaveNoMoreInteractions();
        then(authServerGrpcClient).shouldHaveNoMoreInteractions();
    }

}
