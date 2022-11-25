package org.kie.kogito.jobs.service.api;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.DiscriminatorMapping;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Schema(name = "RecipientDef",
        discriminatorProperty = "type",
        discriminatorMapping = {
                @DiscriminatorMapping(value = "http", schema = HttpRecipient.class),
                @DiscriminatorMapping(value = "kafka", schema = KafkaRecipient.class),
                @DiscriminatorMapping(value = "sink", schema = SinkRecipient.class)
        },
        properties = { @SchemaProperty(name = "type", type = SchemaType.STRING) },
        requiredProperties = { "type" })
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HttpRecipient.class, name = "http"),
        @JsonSubTypes.Type(value = KafkaRecipient.class, name = "kafka"),
        @JsonSubTypes.Type(value = SinkRecipient.class, name = "sink")
})
public abstract class Recipient<T> {

    protected T payload;

    public Recipient() {
    }

    public Recipient(T payload) {
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Recipient{" +
                "payload=" + payload +
                '}';
    }
}
