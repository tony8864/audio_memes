package io.github.tony8864.login.configuration;

import io.github.tony8864.login.application.LoginService;
import io.github.tony8864.login.application.port.IdentityVerifier;
import io.github.tony8864.login.application.port.TokenIssuer;
import io.github.tony8864.login.application.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginConfiguration {

    @Bean
    public LoginService loginService(
            TokenIssuer tokenIssuer,
            UserRepository userRepository,
            IdentityVerifier identityVerifier
    ) {
        return new LoginService(
                tokenIssuer,
                userRepository,
                identityVerifier
        );
    }
}
