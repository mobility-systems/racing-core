package com.theodore.racingcore.exceptions;

import com.theodore.racingmodel.models.MobilityAppErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class RacingCoreExceptionsHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RacingCoreExceptionsHandler.class);


    @ExceptionHandler(InvalidETagException.class)
    public ResponseEntity<MobilityAppErrorResponse> handleInvalidETagException(InvalidETagException ex) {

        LOGGER.error("Entity Tag validation failed : {}", ex.getMessage(), ex);
        MobilityAppErrorResponse error = new MobilityAppErrorResponse("Entity Tag validation failed", Instant.now());

        return new ResponseEntity<>(error, HttpStatus.PRECONDITION_FAILED);
    }

}
