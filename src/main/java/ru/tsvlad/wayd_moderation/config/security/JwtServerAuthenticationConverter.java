package ru.tsvlad.wayd_moderation.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {

    @Override
    public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange)
                .flatMap(s -> Mono.justOrEmpty(s.getRequest().getHeaders().get("Authorization")))
                .filter(stringList -> !stringList.isEmpty())
                .map(stringList -> stringList.get(0))
                .filter(token -> token.startsWith("Bearer "))
                .map(tokenWithBearer -> {
                    String token = tokenWithBearer.substring(7);
                    return new UsernamePasswordAuthenticationToken(token, token);
                });
    }
}
