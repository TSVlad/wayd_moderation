package ru.tsvlad.wayd_moderation.restapi.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tsvlad.wayd_moderation.business.ComplaintProcessing;
import ru.tsvlad.wayd_moderation.config.security.JwtPayload;
import ru.tsvlad.wayd_moderation.document.ComplaintDocument;
import ru.tsvlad.wayd_moderation.enums.ComplaintStatus;
import ru.tsvlad.wayd_moderation.enums.ComplaintType;
import ru.tsvlad.wayd_moderation.restapi.dto.ComplaintDTO;
import ru.tsvlad.wayd_moderation.restapi.dto.ComplaintProcessingDTO;
import ru.tsvlad.wayd_moderation.restapi.dto.ComplaintPublicDTO;
import ru.tsvlad.wayd_moderation.service.ComplaintService;

import java.util.List;

@RestController
@RequestMapping("/complaint")
@AllArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    private final ModelMapper modelMapper;

    @PostMapping
    public Mono<ComplaintPublicDTO> sendComplaint(@RequestBody ComplaintPublicDTO complaintPublicDTO,
                                                  @AuthenticationPrincipal JwtPayload userInfo) {
        ComplaintDocument complaintDocument = modelMapper.map(complaintPublicDTO, ComplaintDocument.class);
        complaintDocument.setComplainingUserId(userInfo.getId());
        return complaintService.createComplaintAndSetModerator(complaintDocument)
                .map(complaint -> modelMapper.map(complaint, ComplaintPublicDTO.class));
    }

    @GetMapping
    public Flux<ComplaintDTO> getComplaintsForModerator(@RequestParam(value = "status", required = false) List<ComplaintStatus> statuses,
                                                        @RequestParam(value = "type", required = false) List<ComplaintType> types,
                                                        @AuthenticationPrincipal JwtPayload userInfo) {
        if (statuses == null) {
            statuses = List.of(ComplaintStatus.values());
        }
        if (types == null) {
            types = List.of(ComplaintType.values());
        }
        return complaintService.getComplaintsForModerator(userInfo.getId(), statuses, types)
                .map(complaintDocument -> modelMapper.map(complaintDocument, ComplaintDTO.class));
    }

    @PostMapping("/process")
    public Mono<ComplaintDTO> processComplaint(@RequestBody ComplaintProcessingDTO complaintProcessingDTO,
                                               @AuthenticationPrincipal JwtPayload userInfo) {
        return complaintService.processComplaint(modelMapper.map(complaintProcessingDTO, ComplaintProcessing.class),
                        userInfo.getId())
                .map(complaintDocument -> modelMapper.map(complaintDocument, ComplaintDTO.class));
    }
}
