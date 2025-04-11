package fr.polytech.isi3.hello.domain.common;

/**
 * Exception thrown when used could not be authenticated.
 */
public class UnauthorizedException extends RuntimeException {

    /**
     * No-arg constructor.
     */
    public UnauthorizedException() {
        super("Unauthorized exception");
    }
}
