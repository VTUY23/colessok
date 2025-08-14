package com.colessok.api.common.constant;

public final class PublicEndpoint {
    public static final String[] POST = {
        "/users/registration", "/auth/token", "/auth/introspect", "/auth/logout", "/auth/refresh",
    };

    public static final String[] GET = {
        "/media/download/**", "/recipes/**",
    };

    private PublicEndpoint() {}
}
