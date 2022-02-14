package ru.tsvlad.wayd_moderation.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tsvlad.wayd_moderation.common.ComplaintProcessing;
import ru.tsvlad.wayd_moderation.document.ComplaintDocument;
import ru.tsvlad.wayd_moderation.enums.ModeratorDecision;
import ru.tsvlad.wayd_moderation.enums.ComplaintStatus;
import ru.tsvlad.wayd_moderation.enums.ComplaintType;
import ru.tsvlad.wayd_moderation.messaging.producer.ModerationServiceProducer;
import ru.tsvlad.wayd_moderation.repository.ComplaintRepository;
import ru.tsvlad.wayd_moderation.service.BanService;
import ru.tsvlad.wayd_moderation.service.ComplaintService;
import ru.tsvlad.wayd_moderation.service.SessionService;

import java.util.List;

@Service
@AllArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;

    private final SessionService sessionService;
    private final BanService banService;

    private final ModerationServiceProducer moderationServiceProducer;

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
    public Flux<ComplaintDocument> getComplaintsForModerator(String moderatorId, List<ComplaintStatus> complaintStatusList,
                                                             List<ComplaintType> types) {
        return complaintRepository.findByModeratorIdAndComplaintStatusInAndTypeIn(moderatorId, complaintStatusList, types);
    }

    @Override
    public Mono<ComplaintDocument> processComplaint(ComplaintProcessing complaintProcessing, String moderatorId) {
        return complaintRepository.findById(complaintProcessing.getComplaintId())
                .flatMap(complaintDocument -> {
                    complaintDocument.process(complaintProcessing);
                    return complaintRepository.save(complaintDocument);
                }).doOnNext(complaintDocument -> {
                    if (complaintProcessing.getComplaintStatus() == ComplaintStatus.SOLVED) {
                        switch (complaintDocument.getType()) {
                            case INVALID_EVENT:
                                processInvalidEvent();
                                break;
                            case INVALID_USER:
                                processInvalidUser();
                                break;
                            case INVALID_IMAGE:
                                processInvalidImage(complaintDocument.getImageDTO().getId(),
                                        complaintProcessing.getModeratorDecision());
                                break;
                        }
                    }
                }).doOnNext(complaintDocument -> {
                    if (complaintProcessing.getBanCreation() != null) {
                        //TODO: owner id
                        complaintProcessing.getBanCreation().setUserId(complaintDocument.getImageDTO().getOwnerId());
                        banService.banUser(complaintProcessing.getBanCreation()).subscribe();
                    }
                });
    }

    private void processInvalidEvent() {
        //send to event later
    }

    private void processInvalidUser() {
        //send to user later
    }

    private void processInvalidImage(String imageId, ModeratorDecision decision) {
        moderationServiceProducer.processImage(imageId, decision);
    }
}
