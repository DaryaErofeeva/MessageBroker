package com.griddynamics.internship.models.db;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class SentMessage {
    private int id;

    @NonNull
    private Consumer consumer;

    @NonNull
    private Message message;

    @NonNull
    private String state;

    @NonNull
    private Timestamp timestamp;
}
