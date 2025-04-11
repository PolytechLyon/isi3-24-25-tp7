package fr.polytech.isi3.hello.domain.login;

import fr.polytech.isi3.hello.domain.user.User;

/**
 * Connected user's information.
 *
 * @param username  the username
 * @param locale    the user's locale
 */
public record UserInfo(
        String username,
        String locale
) {

    /**
     * Create an instance from a user.
     *
     * @param user  a {@link User} instance
     * @return      a corresponding {@link UserInfo} instance
     */
    public static UserInfo from(User user) {
        return new UserInfo(user.username(), user.locale());
    }
}
