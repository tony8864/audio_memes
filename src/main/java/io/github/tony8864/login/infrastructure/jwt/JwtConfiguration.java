package io.github.tony8864.login.infrastructure.jwt;

import io.github.tony8864.login.application.port.TokenIssuer;
import io.github.tony8864.login.infrastructure.jwt.pem.PemKeyType;
import io.github.tony8864.login.infrastructure.jwt.pem.PemParser;
import io.github.tony8864.login.infrastructure.jwt.pem.PemSource;
import io.github.tony8864.login.infrastructure.jwt.pem.PemSourceFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.time.Clock;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfiguration {

    @Bean
    public TokenIssuer jwtTokenIssuer(
            RSAPrivateKey privateKey,
            JwtProperties properties,
            Clock clock
    ) {
        return new JwtTokenIssuer(privateKey, properties, clock);
    }

    @Bean
    public RSAPrivateKey jwtPrivateKey() throws Exception {
        PemSource pemSource = PemSourceFactory.getSource(PemKeyType.PRIVATE_KEY);
        return PemParser.parsePrivateKey(pemSource);
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
