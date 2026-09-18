package io.github.tony8864.login.application.port;

import io.github.tony8864.login.application.model.AccessToken;
import io.github.tony8864.login.domain.User;

public interface TokenIssuer {
    AccessToken issueFor(User user);
}
