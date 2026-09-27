package io.github.tony8864.login.infrastructure.verifier.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.github.tony8864.login.application.port.IdentityVerifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(GoogleVerifierProperties.class)
public class GoogleVerifierConfiguration {

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier(GoogleVerifierProperties properties) {
        return new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance()
        )
                .setAudience(List.of(properties.clientId()))
                .build();
    }

    @Bean
    public IdentityVerifier googleIdentityVerifier(GoogleIdTokenVerifier googleIdTokenVerifier) {
        return new GoogleIdentityVerifier(googleIdTokenVerifier);
    }
}
