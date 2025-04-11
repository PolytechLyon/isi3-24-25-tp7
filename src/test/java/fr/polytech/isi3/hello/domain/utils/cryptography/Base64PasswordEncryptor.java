package fr.polytech.isi3.hello.domain.utils.cryptography;

import java.util.Base64;

public class Base64PasswordEncryptor implements PasswordEncrypter {

    @Override
    public String encrypt(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }
}
