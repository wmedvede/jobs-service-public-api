package org.kie.kogito.jobs.service.api;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "The retry configuration establishes the number of times a failing Job execution must be retried before it’s considered as FAILED")
public class Retry {

    private int maxRetries;
    private long delay;
    private TemporalUnit delayUnit;
    private long maxDuration;
    private TemporalUnit durationUnit;

    public Retry() {
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public TemporalUnit getDelayUnit() {
        return delayUnit;
    }

    public void setDelayUnit(TemporalUnit delayUnit) {
        this.delayUnit = delayUnit;
    }

    public long getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(long maxDuration) {
        this.maxDuration = maxDuration;
    }

    public TemporalUnit getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(TemporalUnit durationUnit) {
        this.durationUnit = durationUnit;
    }

    @Override
    public String toString() {
        return "Retry{" +
                "maxRetries=" + maxRetries +
                ", delay=" + delay +
                ", delayUnit='" + delayUnit + '\'' +
                ", maxDuration=" + maxDuration +
                ", durationUnit='" + durationUnit + '\'' +
                '}';
    }
}
