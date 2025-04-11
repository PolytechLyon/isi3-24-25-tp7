package fr.polytech.isi3.hello.domain.user;

/**
 * User entity.
 *
 * @param username  the username
 * @param password  the password, usually encrypted
 * @param locale    the user's locale
 */
public record User(
        String username,
        String password,
        String locale
) {

    /**
     * Clone user with new password.
     *
     * @param password  the new password
     * @return          a new user with the new password
     */
    public User withPassword(String password) {
        return new User(username, password, locale);
    }

    /**
     * Clone used with new username.
     *
     * @param username  the new username
     * @return          a new user with the new username
     */
    public User withUsername(String username) {
        return new User(username, password, locale);
    }
}
