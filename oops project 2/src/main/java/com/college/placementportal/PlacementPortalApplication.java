package com.college.placementportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlacementPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlacementPortalApplication.class, args);
        System.out.println("=================================================");
        System.out.println(" Placement Preparation Portal Started on port 8080");
        System.out.println(" Access URL: http://localhost:8080");
        System.out.println(" Default Student: arun@college.edu / student123");
        System.out.println(" Default Admin: admin@college.edu / admin123");
        System.out.println("=================================================");
    }
}
