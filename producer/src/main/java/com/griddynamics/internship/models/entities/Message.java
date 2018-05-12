package com.griddynamics.internship.models.entities;

import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Message {
    private int id;

    @NonNull
    private String content;

    @NonNull
    private String state;

    private Timestamp timestamp;
}
