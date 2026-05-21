package com.cloudteamprofileapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CloudTeamProfileApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudTeamProfileApiApplication.class, args);
    }

}
