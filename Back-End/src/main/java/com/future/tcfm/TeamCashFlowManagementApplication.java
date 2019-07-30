package com.future.tcfm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class TeamCashFlowManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeamCashFlowManagementApplication.class, args);
    }

}
