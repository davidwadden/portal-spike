package io.pivotal.cnde.portal.spike.order;

import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders/{machineUuid}")
    public Map<String, Object> getOrder(@PathVariable String machineUuid) {
        StateMachine<OrderStates, OrderEvents> stateMachine = orderService.getOrder(UUID.fromString(machineUuid));


        return new HashMap<String, Object>() {{
            put("id", stateMachine.getId());
            put("uuid", stateMachine.getUuid());
            put("stateName", stateMachine.getState().getId().name());
        }};
    }

}
