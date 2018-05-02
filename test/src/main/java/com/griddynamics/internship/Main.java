package com.griddynamics.internship;

import com.griddynamics.internship.dao.DAOFactory;
import com.griddynamics.internship.models.Message;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;

import java.io.IOException;
import java.nio.file.Paths;

//@SpringBootConfiguration
public class Main {//implements CommandLineRunner {
//    @Autowired
//    private WatcherService watcherService;

    public static void main(String[] args) throws IOException, InterruptedException {
//        SpringApplication app = new SpringApplication(Main.class);
//        app.setBannerMode(Banner.Mode.OFF);
//        app.run(args);

            if (args != null)
                new WatcherService(Paths.get(args[0])).processEvents();
    }

//    @Override
//    public void run(String... args) throws Exception {
//        watcherService.poll();
//    }
}
