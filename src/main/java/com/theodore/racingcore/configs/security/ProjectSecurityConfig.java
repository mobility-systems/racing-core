package com.theodore.racingcore.configs.security;

import com.theodore.infrastructure.common.models.OAuth2ResourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ClientCredentialsOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.RestClientClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Duration;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(OAuth2ResourceProperties.class)
public class ProjectSecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter)
            throws Exception {
        http
                .csrf(csrfConfig -> csrfConfig
                        .ignoringRequestMatchers(request ->
                                (
                                        request.getServletPath().startsWith("/cars/")
                                                || request.getServletPath().startsWith("/drivetrain/")
                                                || request.getServletPath().startsWith("/manufacturers/")
                                                || request.getServletPath().startsWith("/general-entries/")
                                                || request.getServletPath().startsWith("/racing/")
                                )
                        )
                )
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/v3/api-docs.yaml"
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET,
                                        "/cars/by-id/**",
                                        "/cars/model/by-id/**"
                                )
                                .permitAll()
                                .requestMatchers(HttpMethod.GET,
                                        "/drivetrain/by-id/**",
                                        "/drivetrain/engine/by-id/**"
                                ).permitAll()
                                .requestMatchers(
                                        "/manufacturers/by-id/**",
                                        "/manufacturers/search"
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET,
                                        "/racing/track/by-id/**",
                                        "/racing/driver/by-id/**"
                                ).permitAll()

                                .anyRequest().permitAll()
                        //.anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
                );

        return http.build();
    }

    @Bean
    Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter gac = new JwtGrantedAuthoritiesConverter();
        gac.setAuthoritiesClaimName("roles");
        gac.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jac = new JwtAuthenticationConverter();
        jac.setJwtGrantedAuthoritiesConverter(gac);
        return jac;
    }

    private static final Map<String, String> REGISTRATION_RESOURCES = Map.of(
            "account-management", "urn:mobility:account-management",
            "auth-server-grpc", "urn:mobility:auth-server"
    );

    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrations,
            OAuth2AuthorizedClientService clientService,
            OAuth2ResourceProperties resourceProperties) {

        var tokenClient = new RestClientClientCredentialsTokenResponseClient();
        tokenClient.addParametersConverter(grantRequest -> {
            String resource = REGISTRATION_RESOURCES.get(
                    grantRequest.getClientRegistration().getRegistrationId());
            if (resource == null) {
                return new LinkedMultiValueMap<>();
            }
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("resource", resource);
            return params;
        });

        var provider = new ClientCredentialsOAuth2AuthorizedClientProvider();
        provider.setAccessTokenResponseClient(tokenClient);
        provider.setClockSkew(Duration.ofSeconds(30));

        var manager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrations, clientService);

        manager.setAuthorizedClientProvider(provider);
        return manager;
    }

}