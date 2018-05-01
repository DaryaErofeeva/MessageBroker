package com.griddynamics.internship;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource(value = "classpath:my.properties")
public class ReaderService {
    @Value("${rootFolder}")
    private String rootFolder;

    public String getRootFolder() {
        return rootFolder;
    }
}
