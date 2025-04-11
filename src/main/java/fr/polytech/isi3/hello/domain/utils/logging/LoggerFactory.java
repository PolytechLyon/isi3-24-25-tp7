package fr.polytech.isi3.hello.domain.utils.logging;

import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class LoggerFactory {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Logger getLogger(InjectionPoint injectionPoint) {
        String name = injectionPoint.getMember().getDeclaringClass().getSimpleName();
        return new ConsoleLogger(name);
    }
}
