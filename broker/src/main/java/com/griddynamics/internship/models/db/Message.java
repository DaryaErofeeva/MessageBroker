package com.griddynamics.internship.models.db;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Message {
    private int id;

    @NonNull
    private String name;

    @NonNull
    private String content;

    @NonNull
    private Timestamp timestamp;
}
