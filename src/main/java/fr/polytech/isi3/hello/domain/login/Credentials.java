package fr.polytech.isi3.hello.domain.login;

/**
 * User credentials.
 */
public record Credentials(
        String username,
        String password
) { }
