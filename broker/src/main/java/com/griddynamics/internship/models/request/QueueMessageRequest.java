package com.griddynamics.internship.models.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

import java.sql.Timestamp;

@JsonRootName("queue_message")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class QueueMessageRequest {
    private int id;

    @NonNull
    private int messageId;

    @NonNull
    private String state;
}
