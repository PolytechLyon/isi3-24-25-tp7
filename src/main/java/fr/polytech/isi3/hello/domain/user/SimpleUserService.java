package fr.polytech.isi3.hello.domain.user;

import fr.polytech.isi3.hello.domain.common.DuplicateKeyException;
import fr.polytech.isi3.hello.domain.common.NotFoundException;
import fr.polytech.isi3.hello.domain.utils.cryptography.PasswordEncrypter;
import fr.polytech.isi3.hello.domain.utils.logging.Logger;
import java.util.Optional;
import java.util.function.UnaryOperator;

import org.springframework.stereotype.Service;

@Service
public class SimpleUserService implements UserService {

    private final UserRepository repository;
    private final PasswordEncrypter encrypter;
    private final Logger logger;

    public SimpleUserService(
           UserRepository repository,
           PasswordEncrypter encrypter,
           Logger logger
    ) {
        this.repository = repository;
        this.encrypter = encrypter;
        this.logger = logger;
    }

    @Override
    public User create(User user) throws DuplicateKeyException {
        this.logger.log("Creating user %s", user.username());
        return this.mutate(user, this.repository::create);
    }

    @Override
    public User update(User user) throws NotFoundException {
        this.logger.log("Updating user %s", user.username());
        return this.mutate(user, this.repository::update);
    }

    private User mutate(User user, UnaryOperator<User> mutation) {
        String password = this.encrypter.encrypt(user.password());
        return mutation.apply(user.withPassword(password));
    }

    @Override
    public void delete(String username) throws NotFoundException {
        this.logger.log("Deleting user %s", username);
        repository.delete(username);
    }

    @Override
    public Optional<User> retrieve(String username) {
        this.logger.log("Retrieving user %s", username);
        return this.repository.retrieve(username);
    }
}
