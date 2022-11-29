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

package org.kie.kogito.jobs.service.api;

import java.util.LinkedHashSet;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RecipientDescriptorRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipientDescriptorRegistry.class);
    private final LinkedHashSet<RecipientDescriptor<?>> descriptors = new LinkedHashSet<>();

    private static final RecipientDescriptorRegistry INSTANCE = new RecipientDescriptorRegistry();

    private RecipientDescriptorRegistry() {
        LOGGER.debug("Loading recipient descriptor registry");
        final ServiceLoader<RecipientDescriptor> loader = ServiceLoader.load(RecipientDescriptor.class);
        loader.iterator().forEachRemaining(descriptor -> {
            LOGGER.debug("adding -> ({},{}) to registry", descriptor.getName(), descriptor.getType());
            descriptors.add(descriptor);
        });
        LOGGER.debug("total descriptors: {}", descriptors.size());
    }

    public static RecipientDescriptorRegistry getInstance() {
        return INSTANCE;
    }

    public LinkedHashSet<RecipientDescriptor<?>> getDescriptors() {
        return descriptors;
    }
}
