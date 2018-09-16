package io.pivotal.cnde.portal.spike.order;

import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryStateMachinePersist implements StateMachinePersist<OrderStates, OrderEvents, UUID> {

    private final Map<UUID, StateMachineContext<OrderStates, OrderEvents>> storage = new HashMap<>();

    @Override
    public void write(StateMachineContext<OrderStates, OrderEvents> context, UUID contextObj) throws Exception {
        storage.put(contextObj, context);
    }

    @Override
    public StateMachineContext<OrderStates, OrderEvents> read(UUID contextObj) throws Exception {
        return storage.get(contextObj);
    }
}
