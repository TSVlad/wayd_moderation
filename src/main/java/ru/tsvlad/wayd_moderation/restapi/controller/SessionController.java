package ru.tsvlad.wayd_moderation.restapi.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.tsvlad.wayd_moderation.config.security.JwtPayload;
import ru.tsvlad.wayd_moderation.restapi.dto.SessionDTO;
import ru.tsvlad.wayd_moderation.service.SessionService;

@RestController
@RequestMapping("/session")
@AllArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    private final ModelMapper modelMapper;

    @PostMapping("/start")
    public Mono<SessionDTO> startSession(@AuthenticationPrincipal JwtPayload userInfo) {
        return sessionService.startSession(userInfo.getId()).map(sessionDocument -> modelMapper.map(sessionDocument, SessionDTO.class));
    }

    @PostMapping("/close")
    public Mono<SessionDTO> closeSession(@AuthenticationPrincipal JwtPayload userInfo) {
        return sessionService.closeSession(userInfo.getId()).map(sessionDocument -> modelMapper.map(sessionDocument, SessionDTO.class));
    }
}
