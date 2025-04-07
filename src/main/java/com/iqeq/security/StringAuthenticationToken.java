package com.iqeq.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Objects;

public class StringAuthenticationToken extends AbstractAuthenticationToken {
    private final String token;

    public StringAuthenticationToken(String token) {
        super(null);
        this.token = token;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return getCredentials();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StringAuthenticationToken that = (StringAuthenticationToken) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), token);
    }
}

