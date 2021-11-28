package ru.tsvlad.wayd_moderation.config.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    JwtService<JwtPayload> jwtService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        return Mono.just(authentication)
                .map(auth -> jwtService.validateJwt((String) auth.getCredentials()))
                .map(decryptedToken -> {
                    JwtPayload jwtPayload;
                    try {
                        jwtPayload = jwtService.deserializeTokenTo((String) authentication.getCredentials(), JwtPayload.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    List<SimpleGrantedAuthority> authorities = jwtPayload.getRoles().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    Authentication auth = new UsernamePasswordAuthenticationToken(jwtPayload,
                            authentication.getCredentials(), authorities);
                    return auth;
                }).onErrorResume(e -> {
                    log.info("Error: {} for token {}", e.getMessage(), authentication.getCredentials());
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(null, null);
                    auth.setAuthenticated(false);
                    return Mono.just(auth);
                });
    }
}
