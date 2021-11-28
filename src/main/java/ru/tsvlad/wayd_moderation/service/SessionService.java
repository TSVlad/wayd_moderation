package ru.tsvlad.wayd_moderation.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tsvlad.wayd_moderation.document.SessionDocument;

public interface SessionService {
    Mono<SessionDocument> startSession(long moderatorId);
    Mono<SessionDocument> closeSession(long moderatorId);
    Mono<SessionDocument> getRandomOpenSession();
}
