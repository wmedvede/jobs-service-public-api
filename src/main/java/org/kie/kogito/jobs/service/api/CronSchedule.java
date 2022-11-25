package org.kie.kogito.jobs.service.api;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CronScheduleDef", description = "Schedule definition to execute a job on a cron based configuration", allOf = { Schedule.class })
public class CronSchedule extends Schedule {

    private String expression;
    private String timeZone;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public CronSchedule() {
    }

    public CronSchedule(String expression, String timeZone) {
        this.expression = expression;
        this.timeZone = timeZone;
    }

    @Override
    public String toString() {
        return "CronSchedule{" +
                "expression='" + expression + '\'' +
                ", timeZone='" + timeZone + '\'' +
                '}';
    }
}
