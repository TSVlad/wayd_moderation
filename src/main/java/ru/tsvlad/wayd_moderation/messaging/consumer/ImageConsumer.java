package ru.tsvlad.wayd_moderation.messaging.consumer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.tsvlad.wayd_moderation.document.ComplaintDocument;
import ru.tsvlad.wayd_moderation.messaging.consumer.msg.ImageMessage;
import ru.tsvlad.wayd_moderation.service.ComplaintService;

@Component
@Slf4j
@AllArgsConstructor
public class ImageConsumer {

    private ComplaintService complaintService;

    @KafkaListener(topics = {"image-to-moderation"}, containerFactory = "singleFactory")
    public void consume(ImageMessage message) {
        switch (message.getType()) {
            case INVALID_IMAGE:
                log.info("msg: {}", message);
                complaintService
                        .createComplaintAndSetModerator(ComplaintDocument.createInvalidImageComplaint(message.getImageId()))
                        .subscribe();
                break;
        }
    }
}
