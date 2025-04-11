package fr.polytech.isi3.hello;

import fr.polytech.isi3.hello.domain.user.Locale;
import fr.polytech.isi3.hello.domain.user.User;
import fr.polytech.isi3.hello.domain.user.UserRepository;
import fr.polytech.isi3.hello.domain.utils.cryptography.PasswordEncrypter;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public abstract class BaseIntegrationTest {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncrypter encrypter;

    protected User givenUserExists() {
        Locale[] locales = Locale.values();
        Locale locale = locales[new Random().nextInt(locales.length)];
        return this.givenUserExists(locale.getSymbol());
    }

    protected User givenUserExists(String locale) {
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        User user = new User(username, password, locale);
        this.userRepository.create(new User(username, encrypter.encrypt(password), locale));
        return user;
    }

}
