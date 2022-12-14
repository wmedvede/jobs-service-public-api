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

package org.acme.jobs;

import org.eclipse.microprofile.openapi.OASFactory;
import org.eclipse.microprofile.openapi.OASModelReader;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.media.Discriminator;
import org.eclipse.microprofile.openapi.models.media.Schema;
import org.kie.kogito.jobs.service.api.RecipientDescriptor;
import org.kie.kogito.jobs.service.api.RecipientDescriptorRegistry;
import org.kie.kogito.jobs.service.api.ScheduleDescriptor;
import org.kie.kogito.jobs.service.api.ScheduleDescriptorRegistry;

public class JobServiceModelReader implements OASModelReader {

    private static final String TYPE_PROPERTY_NAME = "type";

    @Override
    public OpenAPI buildModel() {
        return OASFactory.createOpenAPI()
                .components(OASFactory.createComponents()
                        .addSchema("Recipient", buildRecipientSchema())
                        .addSchema("Schedule", buildScheduleSchema()));
    }

    private Schema buildRecipientSchema() {
        Schema schema = buildSchemaWithDiscriminator(TYPE_PROPERTY_NAME)
                .description("Recipient description created by API!")
                .title("Recipient title created by API!");
        Discriminator discriminator = schema.getDiscriminator();
        for (RecipientDescriptor<?> descriptor : RecipientDescriptorRegistry.getInstance().getDescriptors()) {
            discriminator.addMapping(descriptor.getName(), buildLocalSchemaRef(descriptor.getType().getSimpleName()));
        }
        schema.discriminator(discriminator);
        return schema;
    }

    private Schema buildScheduleSchema() {
        Schema schema = buildSchemaWithDiscriminator(TYPE_PROPERTY_NAME)
                .description("Schedule description created by API!")
                .title("Schedule title created by API!");
        Discriminator discriminator = schema.getDiscriminator();
        for (ScheduleDescriptor<?> descriptor : ScheduleDescriptorRegistry.getInstance().getDescriptors()) {
            discriminator.addMapping(descriptor.getName(), buildLocalSchemaRef(descriptor.getType().getSimpleName()));
        }
        return schema;
    }

    private static Schema buildSchemaWithDiscriminator(String discriminatorPropertyName) {
        return OASFactory.createSchema()
                .addProperty(discriminatorPropertyName, OASFactory.createSchema().type(Schema.SchemaType.STRING))
                .addRequired(discriminatorPropertyName)
                .discriminator(OASFactory.createDiscriminator().propertyName(discriminatorPropertyName));
    }

    private static String buildLocalSchemaRef(String name) {
        String template = "#/components/schemas/%s";
        return String.format(template, name);
    }
}
