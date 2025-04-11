package fr.polytech.isi3.hello.domain.common;

/**
 * Exception thrown when a key, such as username, is duplicate.
 */
public class DuplicateKeyException extends RuntimeException {

    /**
     * No-arg constructor.
     */
    public DuplicateKeyException() {
        super("Duplicate key exception");
    }
}
