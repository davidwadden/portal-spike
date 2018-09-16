package io.pivotal.cnde.portal.spike.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.data.jpa.JpaRepositoryStateMachinePersist;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.UUID;

@Configuration
public class StateMachinePersistConfig {

    @Bean
    public StateMachinePersist<OrderStates, OrderEvents, ?> jpaStateMachinePersist(
            JpaStateMachineRepository jpaStateMachineRepository) {
        return new JpaRepositoryStateMachinePersist<>(jpaStateMachineRepository);
    }

    @Bean
    public StateMachinePersister<OrderStates, OrderEvents, UUID> persister(
            StateMachinePersist<OrderStates, OrderEvents, UUID> defaultPersist) {

        return new DefaultStateMachinePersister<>(defaultPersist);
    }

}
