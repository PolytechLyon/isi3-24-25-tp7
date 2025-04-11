package fr.polytech.isi3.hello.domain.user;

import fr.polytech.isi3.hello.domain.common.DuplicateKeyException;
import fr.polytech.isi3.hello.domain.common.NotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Repository for users.
 */
public interface UserRepository {

    /**
     * Retrieve a user by its username.
     *
     * @param username  the username of the user to retrieve
     * @return          the user if it exists, an empty optional otherwise
     */
    Optional<User> retrieve(String username);

    /**
     * Retrieve all users.
     *
     * @return          a list of all users
     */
    List<User> retrieveAll();

    /**
     * Update a user based on their username.
     *
     * @param user      the user to update
     * @return          the updated user
     * @throws NotFoundException if the username is not found
     */
    User update(User user) throws NotFoundException;

    /**
     * Create a new user.
     *
     * @param user      the user to create
     * @return          the created user
     * @throws DuplicateKeyException if the username already exists
     */
    User create(User user) throws DuplicateKeyException;

    /**
     * Delete a user based on their username.
     *
     * @param username      the username of the user to delete
     * @throws NotFoundException if the username is not found
     */
    void delete(String username) throws NotFoundException;
}
