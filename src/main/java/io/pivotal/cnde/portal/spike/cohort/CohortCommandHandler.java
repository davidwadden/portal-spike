package io.pivotal.cnde.portal.spike.cohort;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class CohortCommandHandler {

    private final MessageChannel cohortChannel;

    public CohortCommandHandler(MessageChannel cohortChannel) {
        this.cohortChannel = cohortChannel;
    }

    public void provisionCohort(ProvisionCohortCommand command) {
        Message<ProvisionCohortCommand> message = MessageBuilder
                .withPayload(command)
                .build();

        cohortChannel.send(message);
    }

}
