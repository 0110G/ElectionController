package com.ElectionController;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainClass  /*implements CommandLineRunner*/ {
    public static void main(String[] args) {
        SpringApplication.run(MainClass.class, args);
    }

//    @Override
//    public void run(String... args) {
//        Print("Starting xyz");
//        h2Getter query = new h2Getter();
//        try{
//            Voter v = query.authenticateVoter1("bhavya", "saraf");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        //Print(v.toString());
//    }

    public static void Print(String str) {
        System.out.println(str);
    }

}
