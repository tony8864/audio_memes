package io.github.tony8864.login.application.port;

import io.github.tony8864.login.domain.ExternalIdentity;
import io.github.tony8864.login.domain.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByExternalIdentity(ExternalIdentity externalIdentity);
    void save(User user);
}
