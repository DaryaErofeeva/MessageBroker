package com.griddynamics.internship.models.entities;

import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Consumer {
    private int id;

    @NonNull
    private String port;
}
