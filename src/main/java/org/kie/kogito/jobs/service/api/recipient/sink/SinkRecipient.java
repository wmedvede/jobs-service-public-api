/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.jobs.service.api.recipient.sink;

import io.cloudevents.CloudEvent;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.kie.kogito.jobs.service.api.Recipient;

@Schema(description = "Recipient definition to execute a job with a cloud event on a knative sink", allOf = { Recipient.class })
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
