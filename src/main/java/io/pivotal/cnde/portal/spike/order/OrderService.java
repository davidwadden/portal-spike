package io.pivotal.cnde.portal.spike.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
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
        return restoreState(machineUuid);
    }

    public void createOrder(UUID machineUuid) {

        StateMachine<OrderStates, OrderEvents> machine = factory.getStateMachine(machineUuid.toString());
        machine.start();
        machine.getExtendedState().getVariables().put("orderId", "some-order-id");

        persistState(machine);
    }

    public void payOrder(UUID machineUuid, float paid) {

        StateMachine<OrderStates, OrderEvents> machine = restoreState(machineUuid);

        Message<OrderEvents> payEvent = MessageBuilder
                .withPayload(OrderEvents.PAY)
                .setHeader("paid", paid)
                .build();
        machine.sendEvent(payEvent);
        logger.info("state: {}", machine.getState().getId().name());

        persistState(machine);
    }

    public void fulfillOrder(UUID machineUuid) {

        StateMachine<OrderStates, OrderEvents> machine = restoreState(machineUuid);

        Message<OrderEvents> fulfillEvent = MessageBuilder
                .withPayload(OrderEvents.FULFILL)
                .build();
        machine.sendEvent(fulfillEvent);
        logger.info("state: {}", machine.getState().getId().name());

        persistState(machine);
    }

    public void cancelOrder(UUID machineUuid) {
        StateMachine<OrderStates, OrderEvents> machine = restoreState(machineUuid);

        Message<OrderEvents> cancelEvent = MessageBuilder
                .withPayload(OrderEvents.CANCEL)
                .build();
        machine.sendEvent(cancelEvent);
        logger.info("state: {}", machine.getState().getId().name());

        persistState(machine);
    }

    private void persistState(StateMachine<OrderStates, OrderEvents> machine) {
        try {
            persister.persist(machine, machine.getUuid());
            logger.info("persist(id: {}, uuid: {})", machine.getId(), machine.getUuid());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private StateMachine<OrderStates, OrderEvents> restoreState(UUID machineUuid) {

        StateMachine<OrderStates, OrderEvents> machine = factory.getStateMachine(machineUuid);

        try {
            persister.restore(machine, machine.getUuid());
            logger.info("restore(id: {}, uuid: {})", machine.getId(), machine.getUuid());
            logger.info("state: {}", machine.getState().getId().name());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return machine;
    }
}
