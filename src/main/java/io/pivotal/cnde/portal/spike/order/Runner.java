package io.pivotal.cnde.portal.spike.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Runner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    private final OrderService orderService;
    private final StateMachineFactory<OrderStates, OrderEvents> factory;
    private final StateMachinePersister<OrderStates, OrderEvents, UUID> persister;

    public Runner(OrderService orderService,
                  StateMachineFactory<OrderStates, OrderEvents> factory,
                  StateMachinePersister<OrderStates, OrderEvents, UUID> persister) {
        this.orderService = orderService;
        this.factory = factory;
        this.persister = persister;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        UUID machineUuid = UUID.fromString("00000000-0000-0000-0000-000000000000");

        orderService.createOrder(machineUuid);

        orderService.payOrder(machineUuid, 5.00f);

        StateMachine<OrderStates, OrderEvents> machine = restoreState(machineUuid);

        machine.sendEvent(OrderEvents.FULFILL);
        logger.info("state: {}", machine.getState().getId().name());

        StateMachine<OrderStates, OrderEvents> restoredMachine = factory.getStateMachine(machineUuid);
        persister.restore(restoredMachine, machineUuid);
        logger.info("restore(id: {}, uuid: {})", restoredMachine.getId(), machineUuid);
        logger.info("state: {}", restoredMachine.getState().getId().name());
    }

    private void persistState(StateMachine<OrderStates, OrderEvents> machine) {
        try {
            persister.persist(machine, machine.getUuid());
            logger.info("persist(id: {}, uuid: {})", machine.getId(), machine.getUuid());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("Duplicates")
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
