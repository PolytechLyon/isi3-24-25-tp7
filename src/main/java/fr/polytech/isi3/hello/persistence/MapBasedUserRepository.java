package fr.polytech.isi3.hello.persistence;

import fr.polytech.isi3.hello.domain.common.DuplicateKeyException;
import fr.polytech.isi3.hello.domain.common.NotFoundException;
import fr.polytech.isi3.hello.domain.user.User;
import fr.polytech.isi3.hello.domain.user.UserRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Hash-map-based user repository.
 *
 * This class is used as base class for file-base and in-memory repositories.
 */
public abstract class MapBasedUserRepository implements UserRepository {

    protected final Map<String, String> passwords = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Optional<User> retrieve(String name) {
        return this.passwords.entrySet().stream()
                .filter(e -> name.equals(e.getKey()))
                .map(this::deserialize)
                .findAny();
    }

    @Override
    public List<User> retrieveAll() {
        return this.passwords.entrySet().stream()
                .map(this::deserialize)
                .toList();
    }

    @Override
    public User update(User user) throws NotFoundException {
        String username = this.passwords.keySet().stream()
                .filter(user.username()::equals)
                .findAny()
                .orElseThrow(NotFoundException::new);
        String value = this.serialize(user);
        this.passwords.put(username, value);
        this.commit();
        return user;
    }

    @Override
    public User create(User user) throws DuplicateKeyException {
        if (this.passwords.containsKey(user.username())) {
            throw new DuplicateKeyException();
        }
        String value = this.serialize(user);
        this.passwords.put(user.username(), value);
        this.commit();
        return user;
    }

    @Override
    public void delete(String username) throws NotFoundException {
        if (!this.passwords.containsKey(username)) {
            throw new NotFoundException();
        }
        this.passwords.remove(username);
        this.commit();
    }

    private String serialize(User user) {
        return "%s;%s".formatted(user.password(), user.locale());
    }

    private User deserialize(Map.Entry<String, String> entry) {
        String[] parts = entry.getValue().split(";");
        return new User(entry.getKey(), parts[0], parts[1]);
    }

    protected abstract void commit();
}
