package org.kie.kogito.jobs.service.api;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Defines a job that can be managed by the Jobs Service")
public class Job {

    @Schema(description = "Available states for a Job")
    public enum State {
        TBD1,
        TBD2,
        TBD3
    }

    private String id;
    private String businessKey;
    private State state;
    private Schedule schedule;
    private Retry retry;
    private Recipient recipient;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Retry getRetry() {
        return retry;
    }

    public void setRetry(Retry retry) {
        this.retry = retry;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id='" + id + '\'' +
                ", businessKey='" + businessKey + '\'' +
                ", state=" + state +
                ", schedule=" + schedule +
                ", retry=" + retry +
                ", recipient=" + recipient +
                '}';
    }
}
