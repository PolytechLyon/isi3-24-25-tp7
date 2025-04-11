package fr.polytech.isi3.hello.domain.login;

import fr.polytech.isi3.hello.domain.common.UnauthorizedException;

import java.util.Optional;

/**
 * User login service.
 */
public interface LoginService {

    /**
     * Retrieve connected user with a given token, if any exists.
     *
     * @param token                     connection token
     * @return                          connected user, if any
     */
    Optional<UserInfo> getUserForToken(String token);

    /**
     * Login a user with given credentials.
     *
     * @param credentials               user credentials
     * @return                          connection token information
     * @throws UnauthorizedException    if credentials are invalid
     */
    Token login(Credentials credentials) throws UnauthorizedException;

    /**
     * Logout a user with a given token.
     *
     * @param token                     connection token
     */
    void logout(String token);
}
