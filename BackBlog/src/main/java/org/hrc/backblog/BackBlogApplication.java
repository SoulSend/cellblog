package org.hrc.backblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//启用默认线程池
@EnableAsync
public class BackBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackBlogApplication.class, args);
    }

}
