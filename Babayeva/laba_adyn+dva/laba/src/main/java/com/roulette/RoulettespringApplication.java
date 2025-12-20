package com.roulette;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RoulettespringApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoulettespringApplication.class, args);
        
        System.out.println("---------------------------------------------------------");
        System.out.println("                   Тряска запущена");
        System.out.println("---------------------------------------------------------");
        System.out.println("API доступно по адресу: http://localhost:8080/index.html");
        System.out.println("---------------------------------------------------------");
    }

}