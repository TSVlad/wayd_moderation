package ru.tsvlad.wayd_moderation.restapi.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tsvlad.wayd_moderation.common.ComplaintProcessing;
import ru.tsvlad.wayd_moderation.document.ComplaintDocument;
import ru.tsvlad.wayd_moderation.enums.ComplaintStatus;
import ru.tsvlad.wayd_moderation.enums.ComplaintType;
import ru.tsvlad.wayd_moderation.restapi.dto.ComplaintDTO;
import ru.tsvlad.wayd_moderation.restapi.dto.ComplaintProcessingDTO;
import ru.tsvlad.wayd_moderation.restapi.dto.ComplaintPublicDTO;
import ru.tsvlad.wayd_moderation.service.AuthenticationService;
import ru.tsvlad.wayd_moderation.service.ComplaintService;

import java.util.List;

@RestController
@RequestMapping("/complaint")
@AllArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;
    private final AuthenticationService authenticationService;

    private final ModelMapper modelMapper;

    @PostMapping
    @PreAuthorize("authenticated()")
    public Mono<ComplaintPublicDTO> sendComplaint(@RequestBody ComplaintPublicDTO complaintPublicDTO,
                                                  Authentication authentication) {
        ComplaintDocument complaintDocument = modelMapper.map(complaintPublicDTO, ComplaintDocument.class);
        complaintDocument.setComplainingUserId(authenticationService.getUserId(authentication));
        return complaintService.createComplaintAndSetModerator(complaintDocument)
                .map(complaint -> modelMapper.map(complaint, ComplaintPublicDTO.class));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public Flux<ComplaintDTO> getComplaintsForModerator(@RequestParam(value = "status", required = false) List<ComplaintStatus> statuses,
                                                        @RequestParam(value = "type", required = false) List<ComplaintType> types,
                                                        Authentication authentication) {
        if (statuses == null) {
            statuses = List.of(ComplaintStatus.values());
        }
        if (types == null) {
            types = List.of(ComplaintType.values());
        }
        return complaintService.getComplaintsForModerator(authenticationService.getUserId(authentication), statuses, types)
                .map(complaintDocument -> modelMapper.map(complaintDocument, ComplaintDTO.class));
    }

    @PostMapping("/process")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public Mono<ComplaintDTO> processComplaint(@RequestBody ComplaintProcessingDTO complaintProcessingDTO,
                                               Authentication authentication) {
        return complaintService.processComplaint(modelMapper.map(complaintProcessingDTO, ComplaintProcessing.class),
                        authenticationService.getUserId(authentication))
                .map(complaintDocument -> modelMapper.map(complaintDocument, ComplaintDTO.class));
    }
}
