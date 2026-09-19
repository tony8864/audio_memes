package io.github.tony8864.login.infrastructure;

import io.github.tony8864.login.application.port.UserRepository;
import io.github.tony8864.login.domain.ExternalIdentity;
import io.github.tony8864.login.domain.User;
import io.github.tony8864.login.infrastructure.mapper.UserJpaMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserJpaRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository repository;

    public UserJpaRepositoryAdapter(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findByExternalIdentity(ExternalIdentity externalIdentity) {
        return repository
                .findByAuthProviderAndProviderUserId(
                        externalIdentity.provider(),
                        externalIdentity.providerUserId().value()
                )
                .map(UserJpaMapper::toDomain);
    }

    @Override
    public void save(User user) {
        repository.save(UserJpaMapper.toEntity(user));
    }
}
