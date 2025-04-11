package fr.polytech.isi3.hello.domain.utils.cryptography;

/**
 * Password encrypter functionality.
 */
public interface PasswordEncrypter {

    /**
     * Encrypt input.
     *
     * @param input clear text input
     * @return      encrypted input
     */
    String encrypt(String input);
}
