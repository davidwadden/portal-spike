package io.pivotal.cnde.portal.spike.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

@Component
public class Runner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    private final StateMachineFactory<OrderStates, OrderEvents> factory;

    public Runner(StateMachineFactory<OrderStates, OrderEvents> factory) {
        this.factory = factory;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        StateMachine<OrderStates, OrderEvents> machine = factory.getStateMachine();
        machine.start();
        logger.info("state: {}", machine.getState().getId().name());

        machine.sendEvent(OrderEvents.PAY);
        logger.info("state: {}", machine.getState().getId().name());

        machine.sendEvent(OrderEvents.FULFILL);
        logger.info("state: {}", machine.getState().getId().name());
    }
}
