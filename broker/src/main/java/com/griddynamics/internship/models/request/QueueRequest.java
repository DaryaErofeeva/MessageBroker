package com.griddynamics.internship.models.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

@JsonRootName("queue")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class QueueRequest {
    private int id;

    @NonNull
    private int consumerId;

    @NonNull
    private String name;
}
