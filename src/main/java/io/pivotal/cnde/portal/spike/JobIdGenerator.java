package io.pivotal.cnde.portal.spike;

import java.util.concurrent.atomic.AtomicInteger;

public class JobIdGenerator {

    private final AtomicInteger currentJobId;

    public JobIdGenerator(AtomicInteger currentJobId) {
        this.currentJobId = currentJobId;
    }

    public JobId nextId() {
        return JobId.of(currentJobId.incrementAndGet());
    }
}
