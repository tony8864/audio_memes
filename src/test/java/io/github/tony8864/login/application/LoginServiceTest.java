package io.github.tony8864.login.application;

import io.github.tony8864.login.application.model.AccessToken;
import io.github.tony8864.login.application.model.LoginCommand;
import io.github.tony8864.login.application.model.LoginResult;
import io.github.tony8864.login.application.port.IdentityVerifier;
import io.github.tony8864.login.application.port.TokenIssuer;
import io.github.tony8864.login.application.port.UserRepository;
import io.github.tony8864.login.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private IdentityVerifier verifier;

    @Mock
    private TokenIssuer issuer;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginService service;

    @Test
    void login_userExists_returnsAccessToken() {
        ExternalIdentity externalIdentity = new ExternalIdentity(AuthProvider.GOOGLE, new ProviderUserId("provider-123"));
        User user = new User(UserId.newId(), externalIdentity, Instant.now());
        AccessToken accessToken = new AccessToken("access-123");

        String idToken = "token-123";

        when(verifier.verify(idToken)).thenReturn(externalIdentity);
        when(userRepository.findByExternalIdentity(externalIdentity)).thenReturn(Optional.of(user));
        when(issuer.issueFor(user)).thenReturn(accessToken);

        LoginResult result = service.login(new LoginCommand(idToken));

        assertEquals(accessToken, result.accessToken());
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_newUser_savesUserAndReturnsAccessToken() {
        ExternalIdentity externalIdentity = new ExternalIdentity(AuthProvider.GOOGLE, new ProviderUserId("provider-123"));
        AccessToken accessToken = new AccessToken("access-123");

        String idToken = "token-123";

        when(verifier.verify(idToken)).thenReturn(externalIdentity);
        when(userRepository.findByExternalIdentity(externalIdentity)).thenReturn(Optional.empty());
        when(issuer.issueFor(any(User.class))).thenReturn(accessToken);

        LoginResult result = service.login(new LoginCommand(idToken));

        assertEquals(accessToken, result.accessToken());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();
        assertEquals(externalIdentity, savedUser.externalIdentity());
        verify(issuer).issueFor(savedUser);
    }
}