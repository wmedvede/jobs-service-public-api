package org.kie.kogito.jobs.service.api;

import io.cloudevents.CloudEvent;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "SinkRecipientDef", description = "Recipient definition to execute a job with a cloud event on a knative sink", allOf = { Recipient.class })
public class SinkRecipient extends Recipient<CloudEvent> {

    private String sinkUrl;
    private String contentMode;

    public SinkRecipient() {
    }

    public SinkRecipient(CloudEvent payload,
            String sinkUrl,
            String contentMode) {
        super(payload);
        this.sinkUrl = sinkUrl;
        this.contentMode = contentMode;
    }

    public String getSinkUrl() {
        return sinkUrl;
    }

    public void setSinkUrl(String sinkUrl) {
        this.sinkUrl = sinkUrl;
    }

    public String getContentMode() {
        return contentMode;
    }

    public void setContentMode(String contentMode) {
        this.contentMode = contentMode;
    }

    @Override
    public String toString() {
        return "SinkRecipient{" +
                "sinkUrl='" + sinkUrl + '\'' +
                ", contentMode='" + contentMode + '\'' +
                "} " + super.toString();
    }
}
