package ru.tsvlad.wayd_moderation.config.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class JwtServiceImpl implements JwtService<JwtPayload> {

    @Value("${wayd.jwt.secret}")
    private String jwtSecret;


    private final ObjectMapper objectMapper;
    private final Base64.Decoder decoder = Base64.getDecoder();

    @Autowired
    public JwtServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Jws<Claims> validateJwt(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token);
    }

    @Override
    public JwtPayload deserializeTokenTo(String token, Class<JwtPayload> clazz) throws JsonProcessingException {
        String body = token.split("\\.")[1];
        String jsonBody = new String(decoder.decode(body), StandardCharsets.UTF_8);
        return objectMapper.readValue(jsonBody, JwtPayload.class);
    }
}
