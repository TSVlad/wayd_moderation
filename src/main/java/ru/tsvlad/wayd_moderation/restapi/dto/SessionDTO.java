package ru.tsvlad.wayd_moderation.restapi.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class SessionDTO {
    private String id;
    private long moderatorId;
    private ZonedDateTime start;
    private ZonedDateTime end;
}
