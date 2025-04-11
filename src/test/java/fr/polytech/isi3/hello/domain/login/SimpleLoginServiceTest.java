package fr.polytech.isi3.hello.domain.login;

import fr.polytech.isi3.hello.domain.common.UnauthorizedException;
import fr.polytech.isi3.hello.domain.user.User;
import fr.polytech.isi3.hello.domain.utils.cryptography.Base64PasswordEncryptor;
import fr.polytech.isi3.hello.domain.utils.cryptography.PasswordEncrypter;
import fr.polytech.isi3.hello.persistence.InMemoryUserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SimpleLoginServiceTest {
    private final InMemoryUserRepository userRepository = new InMemoryUserRepository();
    private final PasswordEncrypter encrypter = new Base64PasswordEncryptor();

    private final SimpleLoginService sut = new SimpleLoginService(
            userRepository,
            encrypter
    );

    @Test
    public void testLoginSuccess() {
        // Given
        givenUserExists("test", "tset");

        // When
        Token token = this.sut.login(new Credentials("test", "tset"));

        // Then
        assertNotNull(token);
        assertNotNull(token.secret());
        assertNotEquals("", token.secret());
    }

    @Test
    public void testLoginBadUsername() {
        // Given, When, Then
        assertThrows(UnauthorizedException.class, () -> this.sut.login(new Credentials("test", "taste")));
    }

    @Test
    public void testLoginBadPassword() {
        // Given
        givenUserExists("test", "tset");

        // When, Then
        assertThrows(UnauthorizedException.class, () -> this.sut.login(new Credentials("test", "taste")));
    }

    @Test
    public void testGetUserByToken() {
        // Given
        givenUserExists("test", "tset", "en");
        Token token = this.sut.login(new Credentials("test", "tset"));

        // When
        UserInfo userInfo = this.sut.getUserForToken(token.secret()).orElse(null);

        // Then
        assertNotNull(userInfo);
        assertEquals(userInfo.username(), "test");
        assertEquals(userInfo.locale(), "en");
    }

    @Test
    public void testGetUserByTokenBadToken() {
        // Given, When, Then
        assertTrue(this.sut.getUserForToken("damaged").isEmpty());
    }

    @Test
    public void testLogout() {
        // Given
        givenUserExists("test", "tset", "en");
        Token token = this.sut.login(new Credentials("test", "tset"));

        // When
        this.sut.logout(token.secret());

        // Then
        Optional<UserInfo> opt = this.sut.getUserForToken(token.secret());
        assertTrue(opt.isEmpty());
    }

    private void givenUserExists(String username, String password, String locale) {
        this.userRepository.create(new User(username, encrypter.encrypt(password), locale));
    }

    private void givenUserExists(String username, String password) {
        this.givenUserExists(username, password, "en");
    }

}
