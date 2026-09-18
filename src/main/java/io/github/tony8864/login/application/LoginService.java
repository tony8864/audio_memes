package io.github.tony8864.login.application;

import io.github.tony8864.login.application.model.AccessToken;
import io.github.tony8864.login.application.model.LoginCommand;
import io.github.tony8864.login.application.model.LoginResult;
import io.github.tony8864.login.application.port.IdentityVerifier;
import io.github.tony8864.login.application.port.TokenIssuer;
import io.github.tony8864.login.application.port.UserRepository;
import io.github.tony8864.login.domain.ExternalIdentity;
import io.github.tony8864.login.domain.User;
import io.github.tony8864.login.domain.UserId;

import java.util.Optional;

public class LoginService {
    private final TokenIssuer tokenIssuer;
    private final UserRepository userRepository;
    private final IdentityVerifier identityVerifier;

    public LoginService(
            TokenIssuer tokenIssuer,
            UserRepository userRepository,
            IdentityVerifier identityVerifier
    ) {
        this.tokenIssuer = tokenIssuer;
        this.userRepository = userRepository;
        this.identityVerifier = identityVerifier;
    }

    public LoginResult login(LoginCommand command) {
        ExternalIdentity identity = identityVerifier.verify(command.idToken());

        User user;
        Optional<User> userOptional = userRepository.findByExternalIdentity(identity);

        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            user = new User(UserId.newId(), identity);
            userRepository.save(user);
        }

        AccessToken accessToken = tokenIssuer.issueFor(user);
        return new LoginResult(accessToken);
    }
}
