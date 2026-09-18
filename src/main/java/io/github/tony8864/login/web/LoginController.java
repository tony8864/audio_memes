package io.github.tony8864.login.web;

import io.github.tony8864.login.application.LoginService;
import io.github.tony8864.login.application.model.LoginCommand;
import io.github.tony8864.login.application.model.LoginResult;
import io.github.tony8864.login.web.mapper.LoginWebMapper;
import io.github.tony8864.login.web.model.LoginRequest;
import io.github.tony8864.login.web.model.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginCommand command = LoginWebMapper.toCommand(request);
        LoginResult result = loginService.login(command);
        LoginResponse response = LoginWebMapper.toResponse(result);
        return ResponseEntity.ok(response);
    }
}
