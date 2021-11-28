package ru.tsvlad.wayd_moderation.restapi.dto;

import lombok.Data;
import ru.tsvlad.wayd_moderation.enums.ComplaintStatus;

@Data
public class ComplaintProcessingDTO {
    private String complaintId;
    private ComplaintStatus complaintStatus;
    private String moderatorComment;
}
