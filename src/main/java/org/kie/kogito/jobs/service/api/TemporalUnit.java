package org.kie.kogito.jobs.service.api;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "TemporalUnitDef")
public enum TemporalUnit {
    MILLIS,
    SECONDS,
    MINUTES,
    HOURS,
    DAYS
}
