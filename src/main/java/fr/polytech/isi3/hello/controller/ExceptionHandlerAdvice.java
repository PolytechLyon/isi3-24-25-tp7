package fr.polytech.isi3.hello.controller;

import fr.polytech.isi3.hello.domain.common.NotFoundException;
import fr.polytech.isi3.hello.domain.common.UnauthorizedException;
import fr.polytech.isi3.hello.domain.utils.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception handling configurations.
 */
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    private final Logger logger;

    /**
     * Constructor.
     *
     * @param logger    logger to log exception messages
     */
    public ExceptionHandlerAdvice(Logger logger) {
        this.logger = logger;
    }

    private void log(Exception e) {
        this.logger.log(e.getMessage());
    }

    /**
     * Handle not found exceptions with 404 status code.
     *
     * @param ex        exception to handle
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handle(NotFoundException ex) {
        this.log(ex);
    }

    /**
     * Handle unauthorized exceptions with 401 status code.
     *
     * @param ex        exception to handle
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handle(UnauthorizedException ex) {
        this.log(ex);
    }
}
