package io.pivotal.cnde.portal.spike;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.*;

@Configuration
@EnableBinding(FlowConfig.Cohorts.class)
public class FlowConfig {

    private static final Logger logger = LoggerFactory.getLogger(FlowConfig.class);

    @Bean
    public MessageChannel cohortChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public IntegrationFlow cohortProvisionRequestFlow(@Qualifier("cohort-provision-request") MessageChannel provisionRequest) {
        return IntegrationFlows.from(cohortChannel())
                .log("cohort-provision-request")
                .channel(provisionRequest)
                .get();
    }

    @Bean
    public IntegrationFlow cohortProvisionResponseFlow(@Qualifier("cohort-provision-response") SubscribableChannel provisionResponse) {
        return IntegrationFlows.from(provisionResponse)
                .log("cohort-provision-response")
                .handle(message -> logger.info("handling message: {}", message.getPayload()))
                .get();
    }

    interface Cohorts {

        String PROVISION_REQUEST = "cohort-provision-request";
        String PROVISION_RESPONSE = "cohort-provision-response";

        @Output(PROVISION_REQUEST)
        MessageChannel provisionRequest();

        @Input(PROVISION_RESPONSE)
        SubscribableChannel provisionResponse();
    }

}
