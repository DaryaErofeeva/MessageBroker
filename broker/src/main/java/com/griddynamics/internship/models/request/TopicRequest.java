package com.griddynamics.internship.models.request;


import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

@JsonRootName("topic")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class TopicRequest {
    private int id;

    @NonNull
    private String name;
}
