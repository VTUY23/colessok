package com.colessok.api.identity.configuration;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import com.colessok.api.common.configuration.CustomJwtDecoder;
import com.colessok.api.identity.dto.request.auth.IntrospectRequest;
import com.colessok.api.identity.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomJwtValidator implements JwtDecoder {

    AuthenticationService authenticationService;
    CustomJwtDecoder customJwtDecoder;

    @Override
    public Jwt decode(String token) throws JwtException {
        var response = authenticationService.introspect(
                IntrospectRequest.builder().token(token).build());

        if (!response.isValid()) {
            throw new JwtException("Token invalid");
        }

        return customJwtDecoder.decode(token);
    }
}
