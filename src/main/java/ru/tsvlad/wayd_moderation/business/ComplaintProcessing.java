package ru.tsvlad.wayd_moderation.business;

import lombok.Data;
import ru.tsvlad.wayd_moderation.enums.ComplaintStatus;

@Data
public class ComplaintProcessing {
    private String complaintId;
    private ComplaintStatus complaintStatus;
    private String moderatorComment;
}
