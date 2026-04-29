package com.theodore.racingcore.utils;

import com.theodore.racingcore.integration.TestDataFeeder;
import com.theodore.racingcore.repositories.DriverRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@TestConfiguration
public class RacingCoreTestConfigs {

    @Bean
    public TestDataFeeder testDataFeeder(DriverRepository driverRepository) {
        return new TestDataFeeder(driverRepository);
    }

    @Bean
    @Primary
    JwtDecoder jwtDecoder() {
        return tokenValue -> {
            Instant now = Instant.now();

            Map<String, Object> headers = Map.of("alg", "none");

            Map<String, Object> claims = Map.of(
                    "sub", RacingCoreTestUtils.generateUlId(),
                    "aud", List.of("app-public"),
                    "scope", List.of("openid", "email"),
                    "roles", List.of("SIMPLE_USER"),
                    "iss", "http://localhost:9000/auth-server",
                    "jti", "fafc9d66-b823-44ef-9079-5abd99522ab1",
                    "iat", now.getEpochSecond(),
                    "nbf", now.minusSeconds(5).getEpochSecond(),
                    "exp", now.plusSeconds(3600).getEpochSecond()
            );

            return new Jwt(
                    tokenValue,
                    now.minusSeconds(5),     // issuedAt
                    now.plusSeconds(3600),   // expiresAt
                    headers,
                    claims
            );
        };
    }

}
