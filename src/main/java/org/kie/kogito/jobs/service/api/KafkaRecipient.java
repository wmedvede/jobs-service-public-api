package org.kie.kogito.jobs.service.api;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "KafkaRecipientDef", description = "Recipient definition to execute a job with a kafka message", allOf = { Recipient.class })
public class KafkaRecipient extends Recipient<byte[]> {

    private String bootstrapServers;
    private String topicName;
    private Map<String, String> headers;

    public KafkaRecipient() {
        this.headers = new HashMap<>();
    }

    public KafkaRecipient(byte[] payload,
            String bootstrapServers,
            String topicName,
            Map<String, String> headers) {
        super(payload);
        this.bootstrapServers = bootstrapServers;
        this.topicName = topicName;
        this.headers = headers != null ? headers : new HashMap<>();
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers != null ? headers : new HashMap<>();
    }

    @Override
    public String toString() {
        return "KafkaRecipient{" +
                "bootstrapServers='" + bootstrapServers + '\'' +
                ", topicName='" + topicName + '\'' +
                ", headers=" + headers +
                "} " + super.toString();
    }
}
