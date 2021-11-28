package ru.tsvlad.wayd_moderation.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.tsvlad.wayd_moderation.document.BanDocument;

import java.time.LocalDate;

@Repository
public interface BanRepository extends ReactiveMongoRepository<BanDocument, String> {
    Flux<BanDocument> findAllByUserId(long userId);
    Flux<BanDocument> findAllByBanUntil(LocalDate localDate);
    Flux<BanDocument> findAllByUserIdAndBanUntilAfter(long userId, LocalDate localDate);
}
