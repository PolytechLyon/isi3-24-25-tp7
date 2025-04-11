package fr.polytech.isi3.hello.domain.login;


import fr.polytech.isi3.hello.domain.common.UnauthorizedException;
import fr.polytech.isi3.hello.domain.utils.cryptography.PasswordEncrypter;
import fr.polytech.isi3.hello.domain.user.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class SimpleLoginService implements LoginService {

    private final UserRepository repository;
    private final PasswordEncrypter encrypter;
    private final Map<String, UserInfo> connectedUsers = new HashMap<>();

    public SimpleLoginService(
            UserRepository repository,
            PasswordEncrypter encrypter
    ) {
        this.repository = repository;
        this.encrypter = encrypter;
    }

    @Override
    public Optional<UserInfo> getUserForToken(String token) {
        return Optional.ofNullable(this.connectedUsers.get(token));
    }

    @Override
    public Token login(Credentials credentials) throws UnauthorizedException {
        String username = credentials.username();
        String password = credentials.password();
        // Stored passwords are encrypted
        String encryptedPassword = password;
        UserInfo userInfo = this.repository.retrieve(username)
                .filter(user -> encryptedPassword.equals(user.password()))
                .map(UserInfo::from)
                .orElseThrow(UnauthorizedException::new);
        String secret = UUID.randomUUID().toString();
        this.connectedUsers.put(secret, userInfo);
        return new Token(secret);
    }

    @Override
    public void logout(String token) {
        this.connectedUsers.remove(token);
    }
}
