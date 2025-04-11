package fr.polytech.isi3.hello.controller;

import fr.polytech.isi3.hello.BaseIntegrationTest;
import fr.polytech.isi3.hello.domain.user.SimpleUserService;
import fr.polytech.isi3.hello.domain.user.User;
import fr.polytech.isi3.hello.domain.user.UserService;
import fr.polytech.isi3.hello.domain.utils.cryptography.Base64PasswordEncryptor;
import fr.polytech.isi3.hello.domain.utils.cryptography.PasswordEncrypter;
import fr.polytech.isi3.hello.domain.utils.logging.NoopLogger;
import fr.polytech.isi3.hello.persistence.InMemoryUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = {
                UserController.class,
                ExceptionHandlerAdvice.class,
                SimpleUserService.class,
                InMemoryUserRepository.class,
                Base64PasswordEncryptor.class,
                NoopLogger.class,
        }
)
@EnableWebMvc
@AutoConfigureMockMvc
class UserControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncrypter passwordEncrypter;

    @Test
    public void testRetrieve() throws Exception {
        // Given
        User toRetrieve = this.givenUserExists();

        // When
        mvc.perform(get("/api/users/%s".formatted(toRetrieve.username())))
                .andExpect(status().isOk())

                // Then
                .andExpect(jsonPath("$.username").value(toRetrieve.username()))
                .andExpect(jsonPath("$.locale").value(toRetrieve.locale()));
    }

    @Test
    public void testRetrieveNotFound() throws Exception {
        // When
        mvc.perform(get("/api/users/some_id"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreate() throws Exception {
        // When
        mvc.perform(post("/api/users")
                    .contentType("application/json")
                    .content("""
                        {
                            "username": "test",
                            "password": "test",
                            "locale": "en"
                        }
                        """
                    )
                )
                .andExpect(status().isOk());

        // Then
        User expected = this.userService.retrieve("test").orElse(null);

        assertNotNull(expected);
        assertEquals("en", expected.locale());
        assertEquals(this.passwordEncrypter.encrypt("test"), expected.password());
    }

    @Test
    public void testCreateDuplicate() throws Exception {
        // Given
        User alreadyExists = this.givenUserExists();

        // When
        mvc.perform(post("/api/users")
                    .contentType("application/json")
                    .content("""
                    {
                            "username": "%s",
                            "password": "test",
                            "locale": "en"
                        }
                        """.formatted(alreadyExists.username())
                    )
                )

                // Then
                .andExpect(status().isConflict());
    }

    @Test
    public void testUpdate() throws Exception {
        // Given
        String username = this.givenUserExists().username();

        // When
        mvc.perform(put("/api/users/%s".formatted(username))
                    .contentType("application/json")
                    .content("""
                        {
                            "password": "test",
                            "locale": "en"
                        }
                        """
                    )
                )
                .andExpect(status().isOk());

        // Then
        User expected = this.userService.retrieve(username).orElse(null);

        assertNotNull(expected);
        assertEquals("en", expected.locale());
        assertEquals(this.passwordEncrypter.encrypt("test"), expected.password());
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        // When
        mvc.perform(put("/api/users/some_id")
                        .contentType("application/json")
                        .content("""
                        {
                            "password": "test",
                            "locale": "en"
                        }
                        """
                        )
                )
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception {
        // Given
        String username = this.givenUserExists().username();

        // When
        mvc.perform(delete("/api/users/%s".formatted(username)))
                .andExpect(status().isOk());

        // Then
        User expected = this.userService.retrieve(username).orElse(null);
        assertNull(expected);
    }
}
