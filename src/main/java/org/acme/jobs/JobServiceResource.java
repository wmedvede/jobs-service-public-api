package org.acme.jobs;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.kie.kogito.jobs.service.api.Job;
import org.kie.kogito.jobs.service.api.recipient.sink.SinkRecipient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.cloudevents.CloudEvent;

/**
 * Emulates the Jobs service.
 */
@Path("/jobs")
@ApplicationScoped
public class JobServiceResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceResource.class);

    private final Map<String, Job> currentJobs = new HashMap<>();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Job create(Job job) {
        job.setState(Job.State.TBD1);
        job.setId(UUID.randomUUID().toString());

        if (job.getRecipient() instanceof SinkRecipient) {
            try {
                CloudEvent cloudEvent = ((SinkRecipient) job.getRecipient()).getPayload();
                if ("file.created".equals(cloudEvent.getType())) {
                    Object filePath = cloudEvent.getExtension("filepath");
                    byte[] fileContent = null;
                    if (filePath != null) {
                        fileContent = Files.readAllBytes(Paths.get(URI.create(filePath.toString())));
                        if (Objects.equals(fileContent, cloudEvent.getData().toBytes())) {
                            LOGGER.debug("File content is the expected for file: {}, {}", filePath, new String(fileContent));
                        } else {
                            LOGGER.debug("File content is the expected for file: {}, {}", filePath, new String(fileContent));
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error processing SinkRecipient: {}", job.getRecipient());
            }
        }
        return job;
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Job get(@PathParam("id") String id) {
        Job job = new Job();
        job.setId(id);
        return job;
    }

    @Path("/find")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Job find(@QueryParam("businessKey") @DefaultValue("") String businessKey) {
        return currentJobs.values().stream()
                .filter(job -> businessKey.equals(job.getBusinessKey()))
                .findFirst().orElse(null);
    }
}
