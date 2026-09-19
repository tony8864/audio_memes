package io.github.tony8864.login.web.mapper;

import io.github.tony8864.login.application.model.LoginCommand;
import io.github.tony8864.login.application.model.LoginResult;
import io.github.tony8864.login.web.model.LoginRequest;
import io.github.tony8864.login.web.model.LoginResponse;

public final class LoginWebMapper {

    private LoginWebMapper() {}

    public static LoginCommand toCommand(LoginRequest request) {
        return new LoginCommand(request.idToken());
    }

    public static LoginResponse toResponse(LoginResult result) {
        return new LoginResponse(result.accessToken().value());
    }
}
