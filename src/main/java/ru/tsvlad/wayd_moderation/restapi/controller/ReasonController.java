package ru.tsvlad.wayd_moderation.restapi.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tsvlad.wayd_moderation.document.ReasonDocument;
import ru.tsvlad.wayd_moderation.restapi.dto.ReasonDTO;
import ru.tsvlad.wayd_moderation.service.ReasonService;

@RestController
@RequestMapping("/reason")
@AllArgsConstructor
public class ReasonController {

    private final ReasonService reasonService;

    private final ModelMapper modelMapper;

    @PostMapping
    public Mono<ReasonDTO> saveReason(@RequestBody ReasonDTO reasonDTO) {
        return reasonService.saveReason(modelMapper.map(reasonDTO, ReasonDocument.class))
                .map(reasonDocument -> modelMapper.map(reasonDocument, ReasonDTO.class));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteReason(@PathVariable String id) {
        return reasonService.deleteReason(id);
    }

    @GetMapping
    public Flux<ReasonDTO> getAllReasons() {
        return reasonService.getAllReasons()
                .map(reasonDocument -> modelMapper.map(reasonDocument, ReasonDTO.class));
    }
}
