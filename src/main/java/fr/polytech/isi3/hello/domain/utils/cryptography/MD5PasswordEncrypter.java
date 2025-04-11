package fr.polytech.isi3.hello.domain.utils.cryptography;

import org.springframework.stereotype.Service;

/**
 * MD5-based password encrypter implementation.
 */
@Service
public class MD5PasswordEncrypter extends BasePasswordEncrypter {

    /**
     * Constructor.
     */
    public MD5PasswordEncrypter() {
        super("MD5");
    }
}
