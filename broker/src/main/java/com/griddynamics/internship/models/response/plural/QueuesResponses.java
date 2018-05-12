package com.griddynamics.internship.models.response.plural;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.griddynamics.internship.models.response.QueueResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonRootName("queues")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueuesResponses {
    private List<QueueResponse> queues;
}
