package ru.porodkin.englishgram.bot;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.porodkin.englishgram.service.parser.JsonParser;
import ru.porodkin.englishgram.service.translator.YandexTranslator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class EnglishGram extends TelegramLongPollingBot {

    @Value("${username}")
    private String userName;
    @Value("${token}")
    private String token;

    private final YandexTranslator translator;
    private final JsonParser parser;

    HashMap<String, Set<String>> bd = new HashMap<>();

    public EnglishGram(YandexTranslator translator, JsonParser parser) {
        this.translator = translator;
        this.parser = parser;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            saveWordToDateBase(update);
        }
    }

    private void saveWordToDateBase(Update update) {

        if (update.getMessage().isUserMessage()) {
            String wordToSave = update.getMessage().getText();

            if (wordToSave.toLowerCase().matches(".[a-z]*")) {

                if (bd.containsKey(wordToSave.toLowerCase())) {
                    bd.get(wordToSave.toLowerCase()).stream().forEach(word -> sendMessage(update, word));
                } else {

                    String responseJsonFromYandex = translator.translateWord(wordToSave);
                    String dictionary = translator.dictionaryWords(wordToSave);

                    /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
                    String result = parser.extractText(responseJsonFromYandex);
                    List<String> dictionaryResult = parser.extractDictionary(dictionary);

                    if (result.matches(".[a-zA-Z]*")) {
                        sendMessage(update, result + " - не корректное слово для перевода");
                    } else {
                        Set<String> words = bd.get(wordToSave.toLowerCase());

                        if (words != null) {
                            words.add(result);
                            bd.put(wordToSave.toLowerCase(), words);
                        } else {
                            sendMessage(update, result, dictionaryResult);
                            Set<String> wordsNew = new HashSet<>();
                            wordsNew.add(result);
                            bd.put(wordToSave.toLowerCase(), wordsNew);
                        }
                    }
                }
            } else {
                sendMessage(update, "Неверный формат сообщения!");
            }
        }
    }

    private void sendMessage(Update update, String massage) {
        sendMessage(update, massage, null);
    }

    private void sendMessage(Update update, String massage, List<String> massages) {
        String promo = Strings.lenientFormat("ПЕРЕВОД: %s\nВозможные значения:\n %s \n\n\n«Переведено сервисом «Яндекс.Переводчик» \nhttp://translate.yandex.ru \n«Реализовано с помощью сервиса «API «Яндекс.Словарь» \nhttp://api.yandex.ru/dictionary \n",massage, massages);

        SendMessage incorrectMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(promo);
        try {
            execute(incorrectMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
