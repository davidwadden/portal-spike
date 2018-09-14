package io.pivotal.cnde.portal.spike;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

@Configuration
@EnableBinding(FlowConfig.Cohorts.class)
public class FlowConfig {

    @Bean
    public MessageChannel cohortChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public IntegrationFlow cohortProvisionRequestFlow() {
        return IntegrationFlows.from(cohortChannel())
                .log("cohort-provision-request")
                .channel(Cohorts.PROVISION)
                .get();
    }

    @Bean
    public IntegrationFlow cohortProvisionResponseFlow() {
        return IntegrationFlows.from(Cohorts.PROVISION)
                .log("cohort-provision-response")
                .handle(message -> System.out.println(message.getPayload().toString()))
                .get();
    }

    interface Cohorts {

        String PROVISION = "cohort-provision";

        @Input(PROVISION)
        SubscribableChannel provision();
    }

}
