package ru.porodkin.englishgram.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import ru.porodkin.englishgram.bot.BotStarter;
import ru.porodkin.englishgram.bot.EnglishGram;

@Configuration
public class BotInitConfig {

    @Autowired
    EnglishGram englishGram;

    @Bean
    public TelegramBotsApi telegramBotsApi() {
        return new TelegramBotsApi();
    }

    @Bean
    public void botStarter() {
        BotStarter starter = new BotStarter(telegramBotsApi());
        starter.botStart(englishGram);
    }
}
