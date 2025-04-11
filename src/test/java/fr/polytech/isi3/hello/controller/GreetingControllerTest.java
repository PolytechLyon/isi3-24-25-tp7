package fr.polytech.isi3.hello.controller;

import fr.polytech.isi3.hello.BaseIntegrationTest;
import fr.polytech.isi3.hello.domain.greeting.SimpleGreetingService;
import fr.polytech.isi3.hello.domain.login.Credentials;
import fr.polytech.isi3.hello.domain.login.LoginService;
import fr.polytech.isi3.hello.domain.login.SimpleLoginService;
import fr.polytech.isi3.hello.domain.user.User;
import fr.polytech.isi3.hello.domain.utils.cryptography.Base64PasswordEncryptor;
import fr.polytech.isi3.hello.domain.utils.logging.NoopLogger;
import fr.polytech.isi3.hello.persistence.InMemoryUserRepository;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = {
                GreetingController.class,
                ExceptionHandlerAdvice.class,
                SimpleGreetingService.class,
                NoopLogger.class,
                SimpleLoginService.class,
                InMemoryUserRepository.class,
                Base64PasswordEncryptor.class,
        }
)
@EnableWebMvc
@AutoConfigureMockMvc
class GreetingControllerTest extends BaseIntegrationTest {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private LoginService loginService;

        @ParameterizedTest
        @MethodSource
        public void testGreeting(String locale, String format) throws Exception {
                User user = givenUserExists(locale);
                String token = this.loginService.login(new Credentials(user.username(), user.password())).secret();

                mvc.perform(get("/api/greeting")
                        .sessionAttr(MeController.TOKEN_SESSION_KEY, token))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.message").value(format.formatted(user.username())));
        }

        public static Stream<Arguments> testGreeting() {
                return Stream.of(
                        Arguments.of("en", "Hello %s"),
                        Arguments.of("fr", "Bonjour %s"),
                        Arguments.of("es", "Hola %s"),
                        Arguments.of("de", "Hallo %s"),
                        Arguments.of("it", "Ciao %s"),
                        Arguments.of("ar", "مرحبا %s")
                );
        }

        @Test
        public void testGreetingNoToken() throws Exception {
                mvc.perform(get("/api/greeting"))
                        .andExpect(status().isUnauthorized());
        }

        @Test
        public void testGreetingBadToken() throws Exception {
                mvc.perform(get("/api/greeting")
                        .sessionAttr(MeController.TOKEN_SESSION_KEY, "bad_token"))
                        .andExpect(status().isUnauthorized());
        }

}
