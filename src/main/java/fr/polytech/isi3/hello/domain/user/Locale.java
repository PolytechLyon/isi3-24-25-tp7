package fr.polytech.isi3.hello.domain.user;

import java.util.Arrays;

/**
 * Enumeration of supported user locales.
 */
public enum Locale {
    ENGLISH("en"),
    FRENCH("fr"),
    SPANISH("es"),
    GERMAN("de"),
    ITALIAN("it"),
    ARABIC("ar");

    /**
     * Default locale.
     */
    public static final Locale DEFAULT = ENGLISH;

    private final String symbol;

    Locale(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Locale ISO symbol.
     */
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return this.symbol;
    }

    /**
     * Get locale from ISO symbol.
     * @param symbol    the ISO symbol
     * @return          the corresponding locale
     */
    public static Locale fromSymbol(String symbol) {
        return Arrays.stream(values())
                .filter(locale -> locale.symbol.equals(symbol))
                .findAny()
                .orElse(DEFAULT);
    }
}
