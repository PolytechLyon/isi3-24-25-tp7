package fr.polytech.isi3.hello.controller;

import fr.polytech.isi3.hello.domain.common.UnauthorizedException;
import fr.polytech.isi3.hello.domain.greeting.Greeting;
import fr.polytech.isi3.hello.domain.login.LoginService;
import fr.polytech.isi3.hello.domain.login.UserInfo;
import fr.polytech.isi3.hello.domain.utils.logging.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * Greeting controller.
 */
@RestController
@RequestMapping("/api/greeting")
public class GreetingController {

    private final Logger logger;
    private final LoginService loginService;

    /**
     * Controller constructor.
     *
     * @param logger                logger
     * @param loginService          login service
     */
    public GreetingController(
            Logger logger,
            LoginService loginService
    ) {
        this.logger = logger;
        this.loginService = loginService;
    }

    /**
     * Greeting endpoint.
     *
     * @param token     user token passed as a cookie
     * @return          a greeting object containing a greeting massage
     */
    @GetMapping
    public Greeting greet(
            @SessionAttribute(required = false) String token
    ) {
        this.logger.log("Saying hello");
        return this.loginService.getUserForToken(token)
                .map(UserInfo::username)
                .map("Hi, %s!"::formatted)
                .map(Greeting::new)
                .orElseThrow(UnauthorizedException::new);
    }
}
