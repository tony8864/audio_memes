package io.github.tony8864.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SecurityConfigurationTest.ProtectedController.class)
@Import({
        SecurityConfiguration.class,
        SecurityConfigurationTest.ProtectedController.class
})
class SecurityConfigurationTest {

    @RestController
    public static class ProtectedController {

        @PostMapping("/api/v1/auth/login")
        String login() {
            return "ok";
        }

        @GetMapping("/test/protected")
        String protectedEndpoint(Authentication authentication) {
            return authentication.getName();
        }
    }

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JwtDecoder decoder;

    @Test
    void postLogin_missingBearerToken_returnsOk() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login"))
                .andExpect(status().isOk());
    }

    @Test
    void getProtectedEndpoint_missingBearerToken_returnsUnauthorized()
            throws Exception {

        mockMvc.perform(get("/test/protected"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getProtectedEndpoint_invalidBearerToken_returnsUnauthorized()
            throws Exception {

        when(decoder.decode("invalid-token"))
                .thenThrow(new BadJwtException("Invalid token"));

        mockMvc.perform(get("/test/protected")
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer invalid-token"
                        ))
                .andExpect(status().isUnauthorized());

        verify(decoder).decode("invalid-token");
    }

    @Test
    void getProtectedEndpoint_validBearerToken_returnsAuthenticatedSubject()
            throws Exception {

        Instant issuedAt = Instant.now();

        Jwt jwt = Jwt.withTokenValue("valid-token")
                .header("alg", "RS256")
                .subject("user-id")
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plus(Duration.ofMinutes(15)))
                .build();

        when(decoder.decode("valid-token"))
                .thenReturn(jwt);

        mockMvc.perform(get("/test/protected")
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer valid-token"
                        ))
                .andExpect(status().isOk())
                .andExpect(content().string("user-id"));

        verify(decoder).decode("valid-token");
    }
}