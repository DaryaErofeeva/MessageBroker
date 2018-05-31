package com.griddynamics.internship;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ClockService {

    public Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }
}
