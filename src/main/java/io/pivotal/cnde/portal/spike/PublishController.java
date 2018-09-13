package io.pivotal.cnde.portal.spike;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublishController {

    private static final Logger logger = LoggerFactory.getLogger(PublishController.class);

    private final MessageChannel publishChannel;

    public PublishController(MessageChannel publishChannel) {
        this.publishChannel = publishChannel;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/publish")
    public void publish() {
        Message<String> message = MessageBuilder
                .withPayload("some-message")
                .build();

        logger.info("sending message: {}", message);

        publishChannel.send(message);
    }
}
