package com.griddynamics.internship.models.response.plural;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.griddynamics.internship.models.response.TopicResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonRootName("topics")
@Data
@NoArgsConstructor
public class TopicsResponse {
    private List<TopicResponse> topics;
}
