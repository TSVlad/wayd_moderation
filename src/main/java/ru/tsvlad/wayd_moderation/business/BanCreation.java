package ru.tsvlad.wayd_moderation.business;

import lombok.Data;

@Data
public class BanCreation {
    private String userId;
    private String reason;
    private String comment;
    private int banDuration;
    private String moderatorId;
}
