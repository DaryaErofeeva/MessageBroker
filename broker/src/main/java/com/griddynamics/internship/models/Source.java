package com.griddynamics.internship.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Source {
    private int id;

    @NonNull
    private String name;

    @NonNull
    private List<Consumer> consumers;

    @NonNull
    private List<Message> messages;
}
