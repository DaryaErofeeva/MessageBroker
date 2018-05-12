package com.griddynamics.internship.models.db;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class TopicConsumer {
    private int id;

    @NonNull
    private Topic topic;

    @NonNull
    private Consumer consumer;
}
