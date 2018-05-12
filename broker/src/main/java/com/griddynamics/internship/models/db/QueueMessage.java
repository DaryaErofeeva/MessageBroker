package com.griddynamics.internship.models.db;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class QueueMessage {
    private int id;

    @NonNull
    private String state;

    @NonNull
    private Timestamp timestamp;

    @NonNull
    private Queue queue;

    @NonNull
    private Message message;
}
