package com.theodore.racingcore.exceptions;

public class InvalidETagException extends RuntimeException {

    public InvalidETagException() {
        super("Invalid Entity Tag");
    }

}
