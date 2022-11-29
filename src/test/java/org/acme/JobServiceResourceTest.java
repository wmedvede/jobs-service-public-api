package org.acme;

import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import org.acme.jobs.JobServiceClient;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.kie.kogito.jobs.service.api.*;
import org.kie.kogito.jobs.service.api.recipient.http.HttpRecipient;
import org.kie.kogito.jobs.service.api.recipient.kafka.KafkaRecipient;
import org.kie.kogito.jobs.service.api.recipient.sink.SinkRecipient;
import org.kie.kogito.jobs.service.api.schedule.timer.TimerSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.core.data.PojoCloudEventData;
import io.cloudevents.jackson.JsonCloudEventData;
import io.quarkus.test.junit.QuarkusTest;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class JobServiceResourceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceResourceTest.class);

    @Inject
    @RestClient
    JobServiceClient jobServiceClient;

    @Inject
    ObjectMapper objectMapper;

    @Test
    void createJobHttpRecipient() throws Exception {
        LOGGER.debug("***************************** Test create Job with http recipient: *****************************");

        Job job = createJob();
        byte[] payload = "<user><name>Michael</name><surname>Jackson</surname></user>".getBytes(StandardCharsets.UTF_8);
        HttpRecipient httpRecipient = new HttpRecipient();
        httpRecipient.setPayload(payload);
        httpRecipient.setUrl("http://example.report.server.com");
        httpRecipient.setMethod("POST");
        httpRecipient.getHeaders().put("Content-Type", "application/xml");
        httpRecipient.getQueryParams().put("param1", "value1");
        httpRecipient.getQueryParams().put("param2", "value2");
        job.setRecipient(httpRecipient);

        LOGGER.debug("Job to send:\n {}", asJsons(job));
        Job result = jobServiceClient.create(job);
        LOGGER.debug("Server created job:\n {}", asJsons(result));

        HttpRecipient resultRecipient = (HttpRecipient) result.getRecipient();
        assertThat(result.getBusinessKey()).isEqualTo(job.getBusinessKey());
        assertThat(resultRecipient.getPayload()).isEqualTo(payload);
        assertThat(resultRecipient.getUrl()).isEqualTo(httpRecipient.getUrl());
        assertThat(resultRecipient.getMethod()).isEqualTo(httpRecipient.getMethod());
        assertThat(resultRecipient.getHeaders()).isEqualTo(httpRecipient.getHeaders());
        assertThat(resultRecipient.getQueryParams()).isEqualTo(httpRecipient.getQueryParams());
    }

    @Test
    void createJobKafkaRecipient() throws Exception {
        LOGGER.debug("***************************** Test create Job with kafka recipient: *****************************");

        Job job = createJob();
        byte[] payload = "áö新規".getBytes(StandardCharsets.UTF_8);
        KafkaRecipient kafkaRecipient = new KafkaRecipient();
        kafkaRecipient.setBootstrapServers("localhost:9090");
        kafkaRecipient.setPayload(payload);
        kafkaRecipient.setTopicName("job-events-topic");
        kafkaRecipient.getHeaders().put("header1", "headerValue1");
        kafkaRecipient.getHeaders().put("header2", "headerValue2");
        job.setRecipient(kafkaRecipient);

        LOGGER.debug("Job to send:\n {}", asJsons(job));
        Job result = jobServiceClient.create(job);
        LOGGER.debug("Server created job:\n {}", asJsons(result));

        KafkaRecipient resultRecipient = (KafkaRecipient) result.getRecipient();
        assertThat(result.getBusinessKey()).isEqualTo(job.getBusinessKey());
        assertThat(resultRecipient.getPayload()).isEqualTo(payload);
        assertThat(resultRecipient.getBootstrapServers()).isEqualTo(kafkaRecipient.getBootstrapServers());
        assertThat(resultRecipient.getHeaders()).isEqualTo(kafkaRecipient.getHeaders());
    }

    @Test
    void createJobSinkRecipientWithDataBinary() throws Exception {
        LOGGER.debug("***************************** Test create Job with sink recipient and data binary: *****************************");

        //Arbitrary cloud event with the data containing binary data
        ImmutablePair<Path, byte[]> fileContent = readFileContent("MyFile.txt");
        CloudEvent cloudEvent = CloudEventBuilder.v1().newBuilder()
                .withId(UUID.randomUUID().toString())
                .withSource(URI.create("/invented/file-system"))
                .withDataContentType("application/octet-stream") //not our problem,
                .withType("file.created")
                .withExtension("filename", "MyFile.txt")
                .withExtension("filepath", fileContent.getLeft().toUri())
                .withData(fileContent.getRight())
                .build();

        createJobSinkRecipient(cloudEvent, "Job to send with cloud event with binary content as data:\n {}");
    }

    @Test
    void createJobSinkRecipientWithDataJson() throws Exception {
        LOGGER.debug("***************************** Test create Job with sink recipient and data a json node: *****************************");

        //Arbitrary cloud event with the data containing a json
        ObjectNode customer = objectMapper.createObjectNode();
        customer.put("name", "Michael");
        customer.put("surname", "Jackson");

        CloudEvent jsonCloudEvent = CloudEventBuilder.v1().newBuilder()
                .withId(UUID.randomUUID().toString())
                .withSource(URI.create("/sap/user-system"))
                .withDataContentType("application/json") //not our problem,
                .withType("user.created")
                .withData(JsonCloudEventData.wrap(customer))
                .build();

        createJobSinkRecipient(jsonCloudEvent, "Job to send with cloud event with json node as data:\n {}");
    }

    @Test
    void createJobSinkRecipientWithDataPojo() throws Exception {
        LOGGER.debug("***************************** Test create Job with sink recipient and data a java pojo: *****************************");

        //Arbitrary cloud event with the data containing a pojo.
        Customer customer = new Customer("Michael", "Jackson");

        CloudEvent pojoCloudEvent = CloudEventBuilder.v1().newBuilder()
                .withId(UUID.randomUUID().toString())
                .withSource(URI.create("/sap/user-system"))
                .withDataContentType("application/json") //not our problem,
                .withType("user.created")
                .withData(PojoCloudEventData.wrap(customer, (pojo) -> {
                    try {
                        return objectMapper.writeValueAsBytes(pojo);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }))
                .build();
        createJobSinkRecipient(pojoCloudEvent, "Job to send with cloud event with java pojo as data:\n {}");
    }

    void createJobSinkRecipient(CloudEvent cloudEvent, String message) throws Exception {
        Job job = createJob();

        SinkRecipient sinkRecipient = new SinkRecipient();
        sinkRecipient.setSinkUrl("http://my-kogito-service.default.svc.cluster.local");
        sinkRecipient.setContentMode("binary");
        sinkRecipient.setPayload(cloudEvent);
        job.setRecipient(sinkRecipient);

        LOGGER.debug(message, asJsons(job));
        Job result = jobServiceClient.create(job);
        LOGGER.debug("Server result:\n {}", asJsons(result));

        SinkRecipient resultRecipient = (SinkRecipient) result.getRecipient();
        assertThat(result.getBusinessKey()).isEqualTo(job.getBusinessKey());
        assertSinkRecipient(resultRecipient, sinkRecipient, cloudEvent);
    }

    private void assertSinkRecipient(SinkRecipient recipient, SinkRecipient expectedRecipient, CloudEvent expectedPayload) {
        assertThat(recipient.getSinkUrl()).isEqualTo(expectedRecipient.getSinkUrl());
        assertThat(recipient.getContentMode()).isEqualTo(expectedRecipient.getContentMode());

        CloudEvent payload = recipient.getPayload();
        assertThat(payload.getId()).isEqualTo(expectedPayload.getId());
        assertThat(payload.getType()).isEqualTo(expectedPayload.getType());
        assertThat(payload.getDataContentType()).isEqualTo(expectedPayload.getDataContentType());
        assertThat(payload.getDataSchema()).isEqualTo(expectedPayload.getDataSchema());
        assertThat(payload.getSource()).isEqualTo(expectedPayload.getSource());
        assertThat(payload.getExtensionNames()).isEqualTo(expectedPayload.getExtensionNames());
        for (String extension : payload.getExtensionNames()) {
            assertThat(payload.getExtension(extension)).hasToString((expectedPayload.getExtension(extension).toString()));
        }
        assertThat(payload.getData().toBytes()).isEqualTo(expectedPayload.getData().toBytes());
    }

    private String asJsons(Job job) throws Exception {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(job);
    }

    private Job createJob() {
        Job job = new Job();
        job.setBusinessKey("12-34-56");

        Retry retry = new Retry();
        retry.setDelay(100);
        retry.setDelayUnit(TemporalUnit.SECONDS);
        job.setRetry(retry);

        TimerSchedule timerSchedule = new TimerSchedule();
        timerSchedule.setStartTime("23/11/2022");
        timerSchedule.setRepeatCount(5);
        timerSchedule.setDelay(2);
        timerSchedule.setDelayUnit(TemporalUnit.HOURS);
        job.setSchedule(timerSchedule);

        return job;
    }

    public static ImmutablePair<Path, byte[]> readFileContent(String classPathResource) throws Exception {
        URL url = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(classPathResource),
                "Required test resource was not found in class path: " + classPathResource);
        Path path = Paths.get(url.toURI());
        return ImmutablePair.of(path, Files.readAllBytes(path));
    }
}
