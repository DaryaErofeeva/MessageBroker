package com.griddynamics.internship.models;

import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Consumer {
    private int id;

    @NonNull
    private String host;

    @NonNull
    private String port;
}
