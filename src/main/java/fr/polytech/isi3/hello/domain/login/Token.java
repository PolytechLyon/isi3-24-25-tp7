package fr.polytech.isi3.hello.domain.login;

/**
 * Connection token.
 *
 * @param secret    the secret token
 */
public record Token(
        String secret
) { }
