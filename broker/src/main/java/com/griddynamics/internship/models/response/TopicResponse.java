package com.griddynamics.internship.models.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

@JsonRootName("topic")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class TopicResponse {
    private int id;

    @NonNull
    private String name;
}
