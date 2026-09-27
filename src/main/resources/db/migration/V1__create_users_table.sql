CREATE TABLE users
(
    id UUID NOT NULL,
    auth_provider VARCHAR(32) NOT NULL,
    provider_user_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_external_identity
        UNIQUE (auth_provider, provider_user_id)
);