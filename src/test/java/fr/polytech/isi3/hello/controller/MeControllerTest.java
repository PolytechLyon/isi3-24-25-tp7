package fr.polytech.isi3.hello.controller;

import fr.polytech.isi3.hello.BaseIntegrationTest;
import fr.polytech.isi3.hello.domain.login.Credentials;
import fr.polytech.isi3.hello.domain.login.LoginService;
import fr.polytech.isi3.hello.domain.login.SimpleLoginService;
import fr.polytech.isi3.hello.domain.user.User;
import fr.polytech.isi3.hello.domain.utils.cryptography.Base64PasswordEncryptor;
import fr.polytech.isi3.hello.domain.utils.logging.NoopLogger;
import fr.polytech.isi3.hello.persistence.InMemoryUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = {
                MeController.class,
                ExceptionHandlerAdvice.class,
                SimpleLoginService.class,
                InMemoryUserRepository.class,
                Base64PasswordEncryptor.class,
                NoopLogger.class,
        }
)
@EnableWebMvc
@AutoConfigureMockMvc
class MeControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private LoginService loginService;

    @Test
    public void testLogin() throws Exception {
        User user = givenUserExists();
        mvc.perform(post("/api/me")
                        .contentType("application/json")
                        .content("""
                            {
                                "username": "%s",
                                "password": "%s"
                            }
                            """.formatted(user.username(), user.password())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.secret").isString());
    }

    @Test
    public void testLogout() throws Exception {
        User user = givenUserExists();
        String token = this.loginService.login(new Credentials(user.username(), user.password())).secret();
        assertFalse(this.loginService.getUserForToken(token).isEmpty());
        mvc.perform(delete("/api/me")
                        .sessionAttr(MeController.TOKEN_SESSION_KEY, token))
                .andExpect(status().isOk());

        assertTrue(this.loginService.getUserForToken(token).isEmpty());
    }
}
