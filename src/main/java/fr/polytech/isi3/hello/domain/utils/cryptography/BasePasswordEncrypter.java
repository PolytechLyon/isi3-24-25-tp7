package fr.polytech.isi3.hello.domain.utils.cryptography;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Password encrypter abstract implementation.
 */
public abstract class BasePasswordEncrypter implements PasswordEncrypter {

    protected final String algorithm;

    /**
     * Constructor.
     *
     * @return  Name of the algorithm used for encryption. The name should
     *          follow Java Security Standard Algorithm Names Specification.
     */
    protected BasePasswordEncrypter(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String encrypt(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance(this.algorithm);
            md.update(input.getBytes());
            byte[] digest = md.digest();
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
