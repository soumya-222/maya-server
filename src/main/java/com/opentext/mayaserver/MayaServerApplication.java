package com.opentext.mayaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MayaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MayaServerApplication.class, args);
    }

}
