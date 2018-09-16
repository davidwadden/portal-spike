package io.pivotal.cnde.portal.spike.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RequestMapping("/orders")
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{machineUuid}")
    public Map<String, Object> getOrder(@PathVariable String machineUuid) {

        StateMachine<OrderStates, OrderEvents> stateMachine = orderService.getOrder(UUID.fromString(machineUuid));

        return new HashMap<String, Object>() {{
            put("id", stateMachine.getId());
            put("uuid", stateMachine.getUuid());
            put("stateName", stateMachine.getState().getId().name());
            put("extendedVariables", stateMachine.getExtendedState().getVariables());
        }};
    }

    @PostMapping
    public ResponseEntity<Void> createOrder() {

        UUID machineUuid = UUID.randomUUID();
        orderService.createOrder(machineUuid);

        return buildLocationUri(machineUuid);
    }

    @PostMapping("/{machineUuid}/pay")
    public ResponseEntity<Void> payOrder(@PathVariable String machineUuid, @RequestBody PayOrderDto requestDto) {
        orderService.payOrder(UUID.fromString(machineUuid), requestDto.getPaid());

        return buildLocationUri(UUID.fromString(machineUuid));
    }

    private ResponseEntity<Void> buildLocationUri(UUID machineUuid) {
        HashMap<String, Object> uriVariables = new HashMap<String, Object>() {{
            put("machineUuid", machineUuid);
        }};
        URI locationUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{machineUuid}")
                .build(uriVariables);

        return ResponseEntity
                .created(locationUri)
                .build();
    }

    private static class PayOrderDto {

        private final float paid;

        @JsonCreator
        private PayOrderDto(@JsonProperty("paid") float paid) {
            this.paid = paid;
        }

        public float getPaid() {
            return paid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PayOrderDto that = (PayOrderDto) o;
            return Float.compare(that.paid, paid) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(paid);
        }

        @Override
        public String toString() {
            return "PayOrderDto{" +
                    "paid=" + paid +
                    '}';
        }
    }
}
