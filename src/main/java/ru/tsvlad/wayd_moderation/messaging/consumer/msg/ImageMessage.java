package ru.tsvlad.wayd_moderation.messaging.consumer.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.tsvlad.wayd_moderation.messaging.AbstractMessage;
import ru.tsvlad.wayd_moderation.messaging.consumer.msg.type.ImageMessageType;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ImageMessage extends AbstractMessage {
    private ImageMessageType type;
    private String imageId;
    private byte[] image;
}
