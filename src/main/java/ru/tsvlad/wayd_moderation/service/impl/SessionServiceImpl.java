package ru.tsvlad.wayd_moderation.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.tsvlad.wayd_moderation.document.SessionDocument;
import ru.tsvlad.wayd_moderation.repository.SessionRepository;
import ru.tsvlad.wayd_moderation.restapi.controller.advice.exceptions.ActiveSessionNotFoundException;
import ru.tsvlad.wayd_moderation.restapi.controller.advice.exceptions.SessionAlreadyOpenedException;
import ru.tsvlad.wayd_moderation.service.SessionService;

import java.time.ZonedDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    private final Random random = new Random();

    @Override
    public Mono<SessionDocument> startSession(long moderatorId) {
        return sessionRepository.findByModeratorIdAndEndIsNull(moderatorId)
                .flatMap(sessionDocument -> Mono.<SessionDocument>error(new SessionAlreadyOpenedException()))
                .switchIfEmpty(sessionRepository.save(SessionDocument.builder()
                        .moderatorId(moderatorId)
                        .start(ZonedDateTime.now())
                        .build()));
    }

    @Override
    public Mono<SessionDocument> closeSession(long moderatorId) {
        return sessionRepository.findByModeratorIdAndEndIsNull(moderatorId)
                .flatMap(sessionDocument -> {
                    sessionDocument.setEnd(ZonedDateTime.now());
                    return sessionRepository.save(sessionDocument);
                }).switchIfEmpty(Mono.error(new ActiveSessionNotFoundException()));
    }

    @Override
    public Mono<SessionDocument> getRandomOpenSession() {
        return sessionRepository.findByEndIsNull()
                .collectList()
                .flatMap(list -> {
                    if (list.isEmpty()) {
                        return Mono.empty();
                    }
                    return Mono.just(list.get(random.nextInt(list.size())));
                });
    }
}
