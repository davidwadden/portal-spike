package io.pivotal.cnde.portal.spike.order;

import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStateMachinePersist implements StateMachinePersist<OrderStates, OrderEvents, UUID> {

    private final Map<UUID, StateMachineContext<OrderStates, OrderEvents>> storage = new ConcurrentHashMap<>();

    @Override
    public void write(StateMachineContext<OrderStates, OrderEvents> context, UUID contextObj) throws Exception {
        storage.put(contextObj, context);
    }

    @Override
    public StateMachineContext<OrderStates, OrderEvents> read(UUID contextObj) throws Exception {
        StateMachineContext<OrderStates, OrderEvents> context = storage.get(contextObj);
        if (context == null) {
            throw new IllegalArgumentException("Context not found");
        }
        return context;
    }
}
