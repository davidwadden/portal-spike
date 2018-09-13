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
@EnableBinding(FlowConfig.SomeChannel.class)
public class FlowConfig {

    @Bean
    public MessageChannel publishChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public IntegrationFlow publishFlow() {
        return IntegrationFlows.from(publishChannel())
                .log("publish")
                .channel(SomeChannel.SOME_CHANNEL)
                .get();
    }

    @Bean
    public IntegrationFlow listenFlow() {
        return IntegrationFlows.from(SomeChannel.SOME_CHANNEL)
                .log("listen")
                .handle(message -> System.out.println(message.getPayload().toString()))
                .get();
    }

    interface SomeChannel {

        String SOME_CHANNEL = "some-channel";

        @Input(SOME_CHANNEL)
        SubscribableChannel someChannel();
    }

}
