package io.pivotal.cnde.portal.spike.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Runner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    private final StateMachineFactory<OrderStates, OrderEvents> factory;
    private final StateMachinePersister<OrderStates, OrderEvents, UUID> persister;

    public Runner(StateMachineFactory<OrderStates, OrderEvents> factory,
                  StateMachinePersister<OrderStates, OrderEvents, UUID> persister) {
        this.factory = factory;
        this.persister = persister;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        UUID machineUuid = UUID.fromString("00000000-0000-0000-0000-000000000000");

        StateMachine<OrderStates, OrderEvents> machine = factory.getStateMachine(machineUuid);

        machine.start();
        logger.info("state: {}", machine.getState().getId().name());
        persister.persist(machine, machine.getUuid());
        logger.info("persist(id: {}, uuid: {})", machine.getId(), machineUuid);

        machine.sendEvent(OrderEvents.PAY);
        logger.info("state: {}", machine.getState().getId().name());
        persister.persist(machine, machineUuid);
        logger.info("persist(id: {}, uuid: {})", machine.getId(), machineUuid);

        machine.sendEvent(OrderEvents.FULFILL);
        logger.info("state: {}", machine.getState().getId().name());

        StateMachine<OrderStates, OrderEvents> restoredMachine = factory.getStateMachine(machineUuid);
        persister.restore(restoredMachine, machineUuid);
        logger.info("restore(id: {}, uuid: {})", restoredMachine.getId(), machineUuid);
        logger.info("state: {}", restoredMachine.getState().getId().name());
    }
}
