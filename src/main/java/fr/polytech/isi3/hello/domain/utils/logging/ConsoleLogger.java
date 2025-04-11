package fr.polytech.isi3.hello.domain.utils.logging;

/**
 * Console logger.
 */
public class ConsoleLogger extends NamedLogger {

    /**
     * Constructor.
     *
     * @param name  logger name.
     */
    public ConsoleLogger(String name) {
        super(name);
    }

    @Override
    protected void output(String message) {
        System.out.print(message);
    }
}
