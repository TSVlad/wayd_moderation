package ru.tsvlad.wayd_moderation.document;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.tsvlad.wayd_moderation.business.ComplaintProcessing;
import ru.tsvlad.wayd_moderation.enums.ComplaintStatus;
import ru.tsvlad.wayd_moderation.enums.ComplaintType;
import ru.tsvlad.wayd_moderation.restapi.dto.EventDTO;
import ru.tsvlad.wayd_moderation.restapi.dto.UserDTO;

import java.time.ZonedDateTime;

@Data
@Document(collection = "complaints")
public class ComplaintDocument {
    @Id
    private String id;
    @Version
    private long version;

    @CreatedDate
    private ZonedDateTime created;
    private ZonedDateTime processed;

    private String topic;
    private String message;
    private String reason;

    private long complainingUserId;

    private EventDTO eventDTO;
    private UserDTO userDTO;

    private ComplaintType type;

    private long moderatorId;
    private String moderatorComment;

    private ComplaintStatus complaintStatus = ComplaintStatus.NEW;

    public void process(ComplaintProcessing complaintProcessing) {
        this.complaintStatus = complaintProcessing.getComplaintStatus();
        this.moderatorComment = complaintProcessing.getModeratorComment();
        this.processed = ZonedDateTime.now();
    }
}
