package com.sportybet.settlement.publisher.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportybet.settlement.publisher.presentation.dto.SportEventRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = {"event-outcome"})
@TestPropertySource(properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "kafka.topic.event-outcome=event-outcome"
})
class SportEventPublisherIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldPublishSportEventWithValidAuthentication() throws Exception {
        SportEventRequest request = SportEventRequest.builder()
                .eventId(12345L)
                .eventName("Manchester United vs Liverpool")
                .eventWinnerId(67890L)
                .build();

        mockMvc.perform(post("/api/v1/sport-events")
                        .with(httpBasic("sporty", "sporty"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventId").value(12345))
                .andExpect(jsonPath("$.eventName").value("Manchester United vs Liverpool"))
                .andExpect(jsonPath("$.eventWinnerId").value(67890))
                .andExpect(jsonPath("$.status").value("PUBLISHED"));
    }

    @Test
    void shouldRejectRequestWithoutAuthentication() throws Exception {
        SportEventRequest request = SportEventRequest.builder()
                .eventId(12345L)
                .eventName("Test Event")
                .eventWinnerId(67890L)
                .build();

        mockMvc.perform(post("/api/v1/sport-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldValidateRequiredFields() throws Exception {
        SportEventRequest request = SportEventRequest.builder()
                .eventName("Test Event")
                .build();

        mockMvc.perform(post("/api/v1/sport-events")
                        .with(httpBasic("sporty", "sporty"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.eventId").exists())
                .andExpect(jsonPath("$.eventWinnerId").exists());
    }
}
