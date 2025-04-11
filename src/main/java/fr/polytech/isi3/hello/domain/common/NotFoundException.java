package fr.polytech.isi3.hello.domain.common;

/**
 * Exception thrown when an entity is not found.
 */
public class NotFoundException extends RuntimeException {

    /**
     * No-arg constructor.
     */
    public NotFoundException() {
        super("Not found exception");
    }
}
