package org.kie.kogito.jobs.service.api;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.DiscriminatorMapping;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;

@Schema(name = "ScheduleDef",
        discriminatorProperty = "type",
        discriminatorMapping = {
                @DiscriminatorMapping(value = "cron", schema = CronSchedule.class),
                @DiscriminatorMapping(value = "timer", schema = TimerSchedule.class)
        },
        properties = { @SchemaProperty(name = "type", type = SchemaType.STRING) },
        requiredProperties = { "type" })
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CronSchedule.class, name = "cron"),
        @JsonSubTypes.Type(value = TimerSchedule.class, name = "timer")
})
public abstract class Schedule {

}
