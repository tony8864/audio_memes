package io.github.tony8864.login.web;

import io.github.tony8864.login.application.LoginService;
import io.github.tony8864.login.application.exception.IdentityVerificationException;
import io.github.tony8864.login.application.exception.IdentityVerificationUnavailableException;
import io.github.tony8864.login.application.exception.InvalidIdentityTokenException;
import io.github.tony8864.login.application.exception.TokenIssuingException;
import io.github.tony8864.login.application.model.AccessToken;
import io.github.tony8864.login.application.model.LoginCommand;
import io.github.tony8864.login.application.model.LoginResult;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    LoginService service;

    @Test
    void login_validRequest_returnsAccessToken() throws Exception {
        AccessToken token = new AccessToken("jwt-token");

        when(service.login(any(LoginCommand.class))).thenReturn(new LoginResult(token));

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                    {
                      "idToken": "google-id-token"
                    }
                    """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("jwt-token"));

        ArgumentCaptor<LoginCommand> captor =
                ArgumentCaptor.forClass(LoginCommand.class);

        verify(service).login(captor.capture());

        assertEquals(
                "google-id-token",
                captor.getValue().idToken()
        );
    }

    @Test
    void login_invalidIdentityToken_returnsUnauthorized() throws Exception {
        when(service.login(any(LoginCommand.class))).
                thenThrow(new InvalidIdentityTokenException("Invalid identity token"));

        mockMvc.perform(
                post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "idToken": "invalid-token"
                                }
                                """)
                ).andExpect(status().isUnauthorized());
    }

    @Test
    void login_identityVerificationFails_returnsServiceUnavailable() throws Exception {
        when(service.login(any(LoginCommand.class)))
                .thenThrow(new IdentityVerificationUnavailableException("Failed to verify token"));

        mockMvc.perform(
                post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "idToken": "id-token"
                                }
                                """)
        ).andExpect(status().isServiceUnavailable());
    }

    @Test
    void login_identityVerificationFails_returnsServerInternalError() throws Exception {
        when(service.login(any(LoginCommand.class)))
                .thenThrow(new IdentityVerificationException("Failed to verify token"));

        mockMvc.perform(
                post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "idToken": "id-token"
                                }
                                """)
        ).andExpect(status().isInternalServerError());
    }

    @Test
    void login_tokenIssuingFails_returnsInternalServerError() throws Exception {
        when(service.login(any(LoginCommand.class)))
                .thenThrow(new TokenIssuingException("Failed to issue token"));

        mockMvc.perform(
                post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "idToken": "id-token"
                                }
                                """)
                ).andExpect(status().isInternalServerError());
    }
}