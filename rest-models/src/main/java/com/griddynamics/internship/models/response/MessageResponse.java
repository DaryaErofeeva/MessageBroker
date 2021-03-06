package com.griddynamics.internship.models.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@JsonRootName("message")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private int id;
    private String content;
    private String state;
    private Timestamp timestamp;
}
