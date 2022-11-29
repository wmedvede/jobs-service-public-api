package org.kie.kogito.jobs.service.api;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Schema(discriminatorProperty = "type",
        properties = { @SchemaProperty(name = "type", type = SchemaType.STRING) },
        requiredProperties = { "type" })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class Schedule {

}
