package fr.polytech.isi3.hello.domain.utils.logging;

/**
 * No-op logger.
 */
public class NoopLogger implements Logger {

    public void log(String format, Object... args) {
        // Do nothing
    }
}
