package com.griddynamics.internship.models.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class SourceConsumerMessage {
    private int id;

    @NonNull
    private String state;

    @NonNull
    private Timestamp timestamp;

    @NonNull
    private Source source;

    @NonNull
    private Consumer consumer;

    @NonNull
    private Message message;
}
