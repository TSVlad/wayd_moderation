package ru.tsvlad.wayd_moderation.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.tsvlad.wayd_moderation.enums.BanType;

@Data
@Document(collection = "reasons")
public class ReasonDocument {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private int baseBanDuration;
    private BanType banType;
}
