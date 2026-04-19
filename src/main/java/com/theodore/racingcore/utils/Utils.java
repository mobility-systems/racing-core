package com.theodore.racingcore.utils;

import com.theodore.infrastructure.common.exceptions.InvalidTokenException;
import com.theodore.racingcore.exceptions.InvalidETagException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class Utils {

    private Utils() {
    }

    public static String toEtag(Long version) {
        return "\"" + version + "\"";
    }

    public static long parseIfMatch(String ifMatch) {
        if (ifMatch == null || ifMatch.length() < 2) {
            throw new InvalidETagException();
        }
        var trimmed = ifMatch.replace("\"", "");
        try {
            return Long.parseLong(trimmed);
        } catch (NumberFormatException ex) {
            throw new InvalidETagException();
        }
    }

    public static String getLoggedInUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken auth) || !auth.isAuthenticated() || auth.getToken() == null) {
            throw new InvalidTokenException("Invalid or empty token");
        }
        return auth.getToken().getClaimAsString("sub");
    }

}
