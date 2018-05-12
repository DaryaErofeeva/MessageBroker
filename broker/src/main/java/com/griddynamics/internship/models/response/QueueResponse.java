package com.griddynamics.internship.models.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

@JsonRootName("queue")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class QueueResponse {
    private int id;

    @NonNull
    private ConsumerResponse consumer;

    @NonNull
    private String name;
}
