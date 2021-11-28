package ru.tsvlad.wayd_moderation.restapi.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.tsvlad.wayd_moderation.business.BanCreation;
import ru.tsvlad.wayd_moderation.config.security.JwtPayload;
import ru.tsvlad.wayd_moderation.restapi.dto.BanCreationDTO;
import ru.tsvlad.wayd_moderation.restapi.dto.BanDTO;
import ru.tsvlad.wayd_moderation.service.BanService;

import javax.validation.Valid;

@RestController
@RequestMapping("/ban")
@AllArgsConstructor
public class BanController {
    private final BanService banService;

    private final ModelMapper modelMapper;

    @PostMapping
    public Mono<BanDTO> banUser(@Valid  @RequestBody BanCreationDTO banCreationDTO, @AuthenticationPrincipal JwtPayload jwtPayload) {
        BanCreation banCreation = modelMapper.map(banCreationDTO, BanCreation.class);
        banCreation.setModeratorId(jwtPayload.getId());
        return banService.banUser(banCreation).map(banDocument -> modelMapper.map(banDocument, BanDTO.class));
    }
}
