package ru.porodkin.englishgram.bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;


public class BotStarter {

    private final TelegramBotsApi botsApi;

    public BotStarter(TelegramBotsApi botsApi) {
        this.botsApi = botsApi;
    }

    public void botStart(LongPollingBot bot) {
        ApiContextInitializer.init();
        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
