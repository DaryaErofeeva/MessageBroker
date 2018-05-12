package com.griddynamics.internship.models.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

@JsonRootName("consumer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class ConsumerResponse {
    private int id;

    @NonNull
    private String name;
}
