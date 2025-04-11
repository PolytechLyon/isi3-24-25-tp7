package fr.polytech.isi3.hello.domain.utils.logging;

/**
 * Name logger that is supposed to log its name along with each log entry, to
 * facilitate tracing.
 */
public abstract class NamedLogger implements Logger {
    final protected String name;

    /**
     * Constructor.
     *
     * @param name  logger name.
     */
    protected NamedLogger(String name) {
        this.name = name;
    }

    protected String formatMessage(String format, Object... args) {
        String entry = String.format(format, args);
        return String.format("%s\t%s\n", this.name, entry);
    }

    public void log(String format, Object... args) {
        String message = this.formatMessage(format, args);
        this.output(message);
    }

    protected abstract void output(String message);

}
