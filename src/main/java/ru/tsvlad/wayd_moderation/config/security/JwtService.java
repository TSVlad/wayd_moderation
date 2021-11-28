package ru.tsvlad.wayd_moderation.config.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface JwtService<T> {
    Jws<Claims> validateJwt(String token);

    T deserializeTokenTo(String token, Class<T> clazz) throws JsonProcessingException;
}
