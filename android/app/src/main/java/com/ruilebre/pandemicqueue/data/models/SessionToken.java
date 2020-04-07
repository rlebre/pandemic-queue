package com.ruilebre.pandemicqueue.data.models;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class SessionToken {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DecodedJWT decode() {
        try {
            return JWT.decode(token);
        } catch (JWTDecodeException exception) {
            return null;
        }
    }

    public boolean isValid() {
        Date currentDate = new Date();
        Date expirationDate = decode().getExpiresAt();

        return expirationDate.before(currentDate);
    }

    public boolean isAuthenticated() {
        return (token != null && this.isValid() ? true : false);
    }

    @Override
    public String toString() {
        return "SessionToken{" +
                "token='" + token + '\'' +
                '}';
    }
}
