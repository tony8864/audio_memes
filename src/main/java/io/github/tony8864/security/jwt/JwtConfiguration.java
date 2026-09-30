package io.github.tony8864.security.jwt;

import io.github.tony8864.login.application.port.TokenIssuer;
import io.github.tony8864.security.jwt.pem.PemKeyType;
import io.github.tony8864.security.jwt.pem.PemParser;
import io.github.tony8864.security.jwt.pem.PemSource;
import io.github.tony8864.security.jwt.pem.PemSourceFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.JwtAudienceValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
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
    public JwtDecoder jwtDecoder(
            RSAPublicKey publicKey,
            JwtProperties properties
    ) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(publicKey).build();

        decoder.setJwtValidator(
                new DelegatingOAuth2TokenValidator<>(
                        JwtValidators.createDefaultWithIssuer(properties.issuer()),
                        new JwtAudienceValidator(properties.audience())
                )
        );

        return decoder;
    }

    @Bean
    public RSAPrivateKey jwtPrivateKey() throws Exception {
        PemSource pemSource = PemSourceFactory.getSource(PemKeyType.PRIVATE_KEY);
        return PemParser.parsePrivateKey(pemSource);
    }

    @Bean
    public RSAPublicKey jwtPublicKey() throws Exception {
        PemSource pemSource = PemSourceFactory.getSource(PemKeyType.PUBLIC_KEY);
        return PemParser.parsePublicKey(pemSource);
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
