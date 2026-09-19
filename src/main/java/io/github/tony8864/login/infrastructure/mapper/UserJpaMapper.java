package io.github.tony8864.login.infrastructure.mapper;

import io.github.tony8864.login.domain.ExternalIdentity;
import io.github.tony8864.login.domain.ProviderUserId;
import io.github.tony8864.login.domain.User;
import io.github.tony8864.login.domain.UserId;
import io.github.tony8864.login.infrastructure.entity.UserJpaEntity;

public final class UserJpaMapper {

    private UserJpaMapper() {}

    public static UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(
                user.userId().value(),
                user.externalIdentity().provider(),
                user.externalIdentity().providerUserId().value(),
                user.createdAt()
        );
    }

    public static User toDomain(UserJpaEntity entity) {

        UserId userId = new UserId(entity.getId());
        ExternalIdentity externalIdentity = new ExternalIdentity(
                entity.getAuthProvider(),
                new ProviderUserId(entity.getProviderUserId())
        );

        return new User(
                userId,
                externalIdentity,
                entity.getCreatedAt()
        );
    }
}
