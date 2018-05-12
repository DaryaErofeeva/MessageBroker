package com.griddynamics.internship.models.response.plural;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.griddynamics.internship.models.response.QueueMessageResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonRootName("messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueueMessagesResponse {
    private List<QueueMessageResponse> messages;
}
