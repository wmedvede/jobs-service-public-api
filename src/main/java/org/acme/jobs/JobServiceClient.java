package org.acme.jobs;

import org.kie.kogito.jobs.service.api.Job;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RegisterRestClient
public interface JobServiceClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Job create(Job job);

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Job get(@PathParam("id") String id);
}
