package fr.polytech.isi3.hello.persistence;

import fr.polytech.isi3.hello.domain.utils.logging.Logger;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * A user repository that stores the passwords in a file.
 */
@Repository
public class FileBasedUserRepository extends MapBasedUserRepository {

    private final String fileName;
    private final Logger logger;

    public FileBasedUserRepository(
            @Value("${application.passwords.file:passwords.properties}")
            String fileName,
            Logger logger) {
        this.fileName = fileName;
        this.logger = logger;
    }

    @PostConstruct
    public void init() {
        this.passwords.putAll(this.readFile());
    }

    private Map<String, String> readFile() {
        File file = new File(this.fileName);
        if (!file.exists()) {
            this.logger.log("File %s does not exist", this.fileName);
            return Map.of();
        }
        try {
            InputStream inputStream = new FileInputStream(this.fileName);
            Properties passwords = new Properties();
            passwords.load(inputStream);
            return passwords.entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> e.getKey().toString(),
                            e -> e.getValue().toString()));
        } catch (IOException e) {
            return Map.of();
        }
    }

    @Override
    protected synchronized void commit() {
        Properties passwords = new Properties();
        passwords.putAll(this.passwords);
        try {
            passwords.store(new FileWriter(this.fileName), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
