package com.griddynamics.internship.models.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

import java.sql.Timestamp;

@JsonRootName("topic_message")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class TopicMessageRequest {
    private int id;

    @NonNull
    private int consumerId;

    @NonNull
    private int messageId;

    @NonNull
    private String state;
}
