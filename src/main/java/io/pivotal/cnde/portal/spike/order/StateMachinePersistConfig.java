package io.pivotal.cnde.portal.spike.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.UUID;

@Configuration
public class StateMachinePersistConfig {

    @Bean
    public StateMachinePersist<OrderStates, OrderEvents, UUID> inMemoryPersist() {
        return new InMemoryStateMachinePersist();
    }

    @Bean
    public StateMachinePersister<OrderStates, OrderEvents, UUID> persister(
            StateMachinePersist<OrderStates, OrderEvents, UUID> defaultPersist) {

        return new DefaultStateMachinePersister<>(defaultPersist);
    }
}
