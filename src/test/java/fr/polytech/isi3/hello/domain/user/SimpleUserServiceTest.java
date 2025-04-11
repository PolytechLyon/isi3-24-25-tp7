package fr.polytech.isi3.hello.domain.user;

import fr.polytech.isi3.hello.domain.common.DuplicateKeyException;
import fr.polytech.isi3.hello.domain.common.NotFoundException;
import fr.polytech.isi3.hello.domain.utils.cryptography.Base64PasswordEncryptor;
import fr.polytech.isi3.hello.domain.utils.cryptography.PasswordEncrypter;
import fr.polytech.isi3.hello.domain.utils.logging.NoopLogger;
import fr.polytech.isi3.hello.persistence.InMemoryUserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SimpleUserServiceTest {

    private final InMemoryUserRepository userRepository = new InMemoryUserRepository();
    private final PasswordEncrypter encrypter = new Base64PasswordEncryptor();

    private final SimpleUserService sut = new SimpleUserService(
            userRepository,
            encrypter,
            new NoopLogger()
    );

    @Test
    public void testCreate() {
        // Given
        User toCreate = new User("test", "test", "en");

        // When
        this.sut.create(toCreate);

        // Then
        User expected = toCreate.withPassword(encrypter.encrypt(toCreate.password()));
        User user = this.userRepository.retrieve("test").orElse(null);
        assertNotNull(user);
        assertEquals(expected, user);
    }

    @Test
    public void testCreateDuplicate() {
        // Given
        User toCreate = new User("test", "test", "en");
        this.userRepository.create(toCreate);

        // When, Then
        assertThrows(DuplicateKeyException.class, () -> this.sut.create(toCreate));
    }

    @Test
    public void testRetrieve() {
        // Given
        User toRetrieve = new User("test", encrypter.encrypt("test"), "en");
        this.userRepository.create(toRetrieve);

        // When
        User result = this.sut.retrieve("test").orElse(null);

        // Then
        assertNotNull(result);
        assertEquals(toRetrieve.username(), result.username());
        assertEquals(toRetrieve.locale(), result.locale());
    }

    @Test
    public void testRetrieveNotFound() {
        // Given
        // When
        Optional<User> optUser = this.sut.retrieve("test");

        // Then
        assertTrue(optUser.isEmpty());
    }

    @Test
    public void testUpdate() {
        // Given
        User toUpdate = new User("test", encrypter.encrypt("test"), "en");
        this.userRepository.create(toUpdate);


        // When
        toUpdate = new User("test", "taste", "fr");
        User result = this.sut.update(toUpdate);

        // Then
        assertNotNull(result);
        User user = this.userRepository.retrieve("test").orElse(null);
        assertEquals(user.password(), encrypter.encrypt("taste"));
        assertEquals(user.locale(), toUpdate.locale());
    }

    @Test
    public void testUpdateNotFound() {
        // Given, When, Then
        assertThrows(NotFoundException.class, () -> this.sut.update(new User("test", "test", "fr")));
    }

    @Test
    public void testDelete() {
        // Given
        User toDelete = new User("test", encrypter.encrypt("test"), "en");
        this.userRepository.create(toDelete);

        // When
        this.sut.delete(toDelete.username());

        // Then
        Optional<User> user = this.userRepository.retrieve("test");
        assertTrue(user.isEmpty());
    }

    @Test
    public void testDeleteNotFound() {
        // Given, When, Then
        assertThrows(NotFoundException.class, () -> this.sut.delete("test"));
    }
}
