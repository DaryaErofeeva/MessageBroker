package com.griddynamics.internship.models.response.plural;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.griddynamics.internship.models.response.TopicResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@JsonRootName("topics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicsResponses {
    private List<TopicResponse> topics;
}
