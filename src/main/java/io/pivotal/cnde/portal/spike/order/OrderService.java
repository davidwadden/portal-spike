package io.pivotal.cnde.portal.spike.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final StateMachineFactory<OrderStates, OrderEvents> factory;
    private final StateMachinePersister<OrderStates, OrderEvents, UUID> persister;

    public OrderService(StateMachineFactory<OrderStates, OrderEvents> factory,
                        StateMachinePersister<OrderStates, OrderEvents, UUID> persister) {
        this.factory = factory;
        this.persister = persister;
    }

    public StateMachine<OrderStates, OrderEvents> getOrder(UUID machineUuid) {

        StateMachine<OrderStates, OrderEvents> machine = factory.getStateMachine(machineUuid);

        try {
            persister.restore(machine, machineUuid);
            logger.info("restore(id: {}, uuid: {})", machine.getId(), machine.getUuid());
            logger.info("state: {}", machine.getState().getId().name());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return machine;
    }
}
