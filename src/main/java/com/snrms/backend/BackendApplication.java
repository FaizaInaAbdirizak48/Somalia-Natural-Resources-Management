package com.snrms.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is the entry point of the whole backend.
 * Running this file's main() method starts an embedded web server (Tomcat)
 * on port 8080 and wires together everything Spring finds annotated with
 * @Component, @Service, @Repository, @RestController, etc.
 */
@SpringBootApplication
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
