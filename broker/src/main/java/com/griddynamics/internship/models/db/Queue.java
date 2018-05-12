package com.griddynamics.internship.models.db;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Queue {
    private int id;

    @NonNull
    private String name;

    @NonNull
    private Producer producer;

    @NonNull
    private Consumer consumer;
}
