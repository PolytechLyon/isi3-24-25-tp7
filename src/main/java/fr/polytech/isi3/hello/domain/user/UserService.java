package fr.polytech.isi3.hello.domain.user;

import fr.polytech.isi3.hello.domain.common.DuplicateKeyException;
import fr.polytech.isi3.hello.domain.common.NotFoundException;

import java.util.Optional;

/**
 * User service.
 */
public interface UserService {

    /**
     * Create a new user.
     *
     * @param user  the user to create
     * @return      the created user
     * @throws DuplicateKeyException if the username already exists
     */
    User create(User user) throws DuplicateKeyException;

    /**
     * Update a user.
     *
     * @param user  the user to update
     * @return      the updated user
     * @throws NotFoundException if the user is not found
     */
    User update(User user) throws NotFoundException;

    /**
     * Delete a user.
     *
     * @param username  the username of the user to delete
     * @throws NotFoundException if the user is not found
     */
    void delete(String username) throws NotFoundException;

    /**
     * Retrieve a user by its username.
     *
     * @param username  the username of the user to retrieve
     * @return          the user if it exists, an empty optional otherwise
     */
    Optional<User> retrieve(String username);
}
