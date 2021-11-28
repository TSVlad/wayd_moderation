package ru.tsvlad.wayd_moderation.restapi.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BanCreationDTO {
    @Min(1)
    private long userId;
    @NotBlank
    private String reason;
    @NotNull
    private String comment;
    private int banDuration;
}
