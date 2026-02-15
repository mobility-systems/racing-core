package com.theodore.racingcore.utils;

import com.theodore.racingcore.exceptions.InvalidETagException;
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

}
