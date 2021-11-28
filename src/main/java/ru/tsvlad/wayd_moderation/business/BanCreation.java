package ru.tsvlad.wayd_moderation.business;

import lombok.Data;

@Data
public class BanCreation {
    private long userId;
    private String reason;
    private String comment;
    private int banDuration;
    private long moderatorId;
}
