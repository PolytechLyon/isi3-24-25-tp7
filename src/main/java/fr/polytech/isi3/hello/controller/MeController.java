package fr.polytech.isi3.hello.controller;

import fr.polytech.isi3.hello.domain.common.UnauthorizedException;
import fr.polytech.isi3.hello.domain.login.Credentials;
import fr.polytech.isi3.hello.domain.login.LoginService;
import fr.polytech.isi3.hello.domain.login.Token;
import fr.polytech.isi3.hello.domain.login.UserInfo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * Login controller.
 */
@RestController
@RequestMapping("/api/me")
public class MeController {

    public static final String TOKEN_SESSION_KEY = "token";

    @Autowired
    private LoginService loginService;

    /**
     * Log in user.
     *
     * @param credentials       user credentials
     * @param session           HTTP session
     * @return                  connection token information
     * @throws UnauthorizedException if credentials are invalid
     */
    @PostMapping
    public Token login(
            @RequestBody  final Credentials credentials,
            HttpSession session
    ) throws UnauthorizedException {
        Token token = this.loginService.login(credentials);
        session.setAttribute(TOKEN_SESSION_KEY, token.secret());
        return token;
    }

    /**
     * Retrieve connected user.
     *
     * @param token     user token passed as a cookie
     * @return          connected user
     * @throws UnauthorizedException if no user is connected
     */
    @GetMapping
    public UserInfo retrieve(
            @SessionAttribute(required = false) String token
    ) throws UnauthorizedException {
        return this.loginService.getUserForToken(token)
                .orElseThrow(UnauthorizedException::new);
    }

    /**
     * Log out connected user.
     *
     * @param token    user token passed as a cookie
     */
    @DeleteMapping
    public void logout(
            @SessionAttribute(required = false) String token
    ) {
        this.loginService.logout(token);
    }
}
