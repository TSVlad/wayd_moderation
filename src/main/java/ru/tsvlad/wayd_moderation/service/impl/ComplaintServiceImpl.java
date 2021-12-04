package ru.tsvlad.wayd_moderation.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tsvlad.wayd_moderation.business.ComplaintProcessing;
import ru.tsvlad.wayd_moderation.document.ComplaintDocument;
import ru.tsvlad.wayd_moderation.document.SessionDocument;
import ru.tsvlad.wayd_moderation.enums.ComplaintStatus;
import ru.tsvlad.wayd_moderation.enums.ComplaintType;
import ru.tsvlad.wayd_moderation.repository.ComplaintRepository;
import ru.tsvlad.wayd_moderation.service.ComplaintService;
import ru.tsvlad.wayd_moderation.service.SessionService;

import java.util.List;

@Service
@AllArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;

    private final SessionService sessionService;

    @Override
    public Mono<ComplaintDocument> createComplaintAndSetModerator(ComplaintDocument complaintDocument) {
        return sessionService.getRandomOpenSession()
                .flatMap(session -> {
                    complaintDocument.setModeratorId(session.getModeratorId());
                    return complaintRepository.save(complaintDocument);
                })
                .switchIfEmpty(complaintRepository.save(complaintDocument));
    }

    @Override
    public Flux<ComplaintDocument> getComplaintsForModerator(long moderatorId, List<ComplaintStatus> complaintStatusList, List<ComplaintType> types) {
        return complaintRepository.findByModeratorIdAndComplaintStatusInAndTypeIn(moderatorId, complaintStatusList, types);
    }

    @Override
    public Mono<ComplaintDocument> processComplaint(ComplaintProcessing complaintProcessing, long moderatorId) {
        return complaintRepository.findById(complaintProcessing.getComplaintId())
                .flatMap(complaintDocument -> {
                    complaintDocument.process(complaintProcessing);
                    return complaintRepository.save(complaintDocument);
                });
    }
}
