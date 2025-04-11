package fr.polytech.isi3.hello.domain.greeting;

import fr.polytech.isi3.hello.domain.login.UserInfo;
import fr.polytech.isi3.hello.domain.user.Locale;
import fr.polytech.isi3.hello.domain.utils.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class SimpleGreetingService implements GreetingService {

    private final Logger logger;

    public SimpleGreetingService(Logger logger) {
        this.logger = logger;
    }


    public Greeting greet(UserInfo user) {
        this.logger.log("Greeting user %s", user.username());
        String message = switch (Locale.fromSymbol(user.locale())) {
            case ENGLISH -> "Hello %s";
            case FRENCH -> "Bonjour %s";
            case SPANISH -> "Hola %s";
            case GERMAN -> "Hallo %s";
            case ITALIAN -> "Ciao %s";
            case ARABIC -> "مرحبا %s";
        };

        return new Greeting(message.formatted(user.username()));
    }
}
