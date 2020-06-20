package ru.porodkin.englishgram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class EnglishGramApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(EnglishGramApplication.class, args);
    }
}
