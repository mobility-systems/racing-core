package com.theodore.racingcore.exceptions;

import com.theodore.infrastructure.common.exceptions.AlreadyExistsException;
import com.theodore.infrastructure.common.exceptions.NotFoundException;
import com.theodore.infrastructure.common.exceptions.UserAlreadyExistsException;
import com.theodore.infrastructure.common.models.MobilityAppErrorResponse;
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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MobilityAppErrorResponse> handleNotFoundErrors(NotFoundException ex) {
        LOGGER.warn("Resource not found: {}", ex.getMessage(), ex);
        MobilityAppErrorResponse error = new MobilityAppErrorResponse(ex.getMessage(), Instant.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MobilityAppErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        LOGGER.error("{}", ex.getMessage(), ex);
        MobilityAppErrorResponse error = new MobilityAppErrorResponse(ex.getMessage(), Instant.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<MobilityAppErrorResponse> handleResourceAlreadyExists(AlreadyExistsException ex) {
        LOGGER.warn("{}", ex.getResource(), ex);
        MobilityAppErrorResponse error = new MobilityAppErrorResponse(ex.getMessage(), Instant.now());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

}
