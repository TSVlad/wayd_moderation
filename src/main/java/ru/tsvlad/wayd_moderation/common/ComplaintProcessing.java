package ru.tsvlad.wayd_moderation.common;

import lombok.Data;
import ru.tsvlad.wayd_moderation.enums.ModeratorDecision;
import ru.tsvlad.wayd_moderation.enums.ComplaintStatus;

@Data
public class ComplaintProcessing {
    private String complaintId;
    private String moderatorComment;

    private ComplaintStatus complaintStatus;
}
