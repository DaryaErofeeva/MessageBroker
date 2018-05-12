package com.griddynamics.internship.models.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

import java.sql.Timestamp;

@JsonRootName("topic_message")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class TopicMessageResponse {
    private int id;

    @NonNull
    private ConsumerResponse consumer;

    @NonNull
    private String state;

    @NonNull
    private Timestamp timestamp;
}
