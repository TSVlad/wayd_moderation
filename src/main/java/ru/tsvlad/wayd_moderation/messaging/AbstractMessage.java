package ru.tsvlad.wayd_moderation.messaging;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.tsvlad.wayd_moderation.config.security.JwtPayload;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@SuperBuilder
public abstract class AbstractMessage implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime created;

    private JwtPayload userInfo;
}
