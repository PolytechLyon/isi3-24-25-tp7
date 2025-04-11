package fr.polytech.isi3.hello.controller;

import fr.polytech.isi3.hello.domain.user.Locale;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Locale controller.
 */
@RestController
@RequestMapping("/api/locales")
public class LocaleController {

    /**
     * Retrieve all available locales.
     *
     * @return  a list of locale symbols
     */
    @GetMapping
    public List<String> retrieve() {
        return Stream.of(Locale.values())
                .map(Locale::getSymbol)
                .toList();
    }
}
