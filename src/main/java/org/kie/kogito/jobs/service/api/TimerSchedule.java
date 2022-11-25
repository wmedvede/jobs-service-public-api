package org.kie.kogito.jobs.service.api;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "TimerScheduleDef", description = "Schedule definition to execute a job on a timer based configuration", allOf = { Schedule.class })
public class TimerSchedule extends Schedule {

    private String startTime;
    private int repeatCount;
    private long delay;
    private TemporalUnit delayUnit;

    public TimerSchedule() {
    }

    public TimerSchedule(String startTime, int repeatCount, long delay, TemporalUnit delayUnit) {
        this.startTime = startTime;
        this.repeatCount = repeatCount;
        this.delay = delay;
        this.delayUnit = delayUnit;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
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

    @Override
    public String toString() {
        return "TimerSchedule{" +
                "startTime='" + startTime + '\'' +
                ", repeatCount=" + repeatCount +
                ", delay=" + delay +
                ", delayUnit='" + delayUnit + '\'' +
                '}';
    }
}
