package fr.polytech.isi3.hello.domain.greeting;

import fr.polytech.isi3.hello.domain.login.UserInfo;

/**
 * Greeting service.
 * This service greets users in their native tongue.
 */
public interface GreetingService {

    /**
     * Duly greets the user.
     *
     * @param user  the user to greet
     * @return      the greeting
     */
    Greeting greet(UserInfo user);
}
