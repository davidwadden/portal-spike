package io.pivotal.cnde.portal.spike;

import java.util.Objects;

public class JobId {

    private final int value;

    private JobId(int value) {
        this.value = value;
    }

    public static JobId of(int jobId) {
        return new JobId(jobId);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobId jobId1 = (JobId) o;
        return value == jobId1.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "JobId{" +
                "value=" + value +
                '}';
    }
}
