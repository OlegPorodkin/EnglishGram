package ru.porodkin.englishgram.bot;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Component
public class EnglishGram extends TelegramLongPollingBot {

    @Value("${username}")
    private String userName;
    @Value("${token}")
    private String token;

    HashMap<String, Set<String>> bd = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            saveWordToDateBase(update);

            bd.entrySet().stream().forEach(e -> {
                System.out.println(e.getKey() + " : " + e.getValue());
            });
        }
    }

    private void saveWordToDateBase(Update update) {

        if (update.getMessage().isUserMessage()) {
            StringBuffer result = null;
            String wordToSave = update.getMessage().getText();

            if (wordToSave.toLowerCase().matches(".[a-z]*")) {

                if (bd.containsKey(wordToSave)) {
                    bd.get(wordToSave).stream().forEach(word -> sendMessage(update, word));
                } else {
                    /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

                    try {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost post = new HttpPost("https://www.webtran.ru/gtranslate/");

                        List<NameValuePair> urlParameters = new ArrayList<>();
                        urlParameters.add(new BasicNameValuePair("text", wordToSave));
                        urlParameters.add(new BasicNameValuePair("gfrom", "en"));
                        urlParameters.add(new BasicNameValuePair("gto", "ru"));
                        urlParameters.add(new BasicNameValuePair("key", "630340450ru5"));

                        post.setEntity(new UrlEncodedFormEntity(urlParameters));

                        HttpResponse response = httpClient.execute(post);

                        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        result = new StringBuffer();
                        String line = "";
                        while ((line = rd.readLine()) != null) {
                            result.append(line);
                        }

                        sendMessage(update, result.toString());

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
                    if (result.toString().matches(".[a-zA-Z]*")) {
                        sendMessage(update, result.toString() + " - не корректное слово для перевода");
                    } else {
                        Set<String> words = bd.get(wordToSave.toLowerCase());
                        if (words != null) {
                            words.add(result.toString());
                            bd.put(wordToSave.toLowerCase(), words);
                        } else {
                            Set<String> wordsNew = new HashSet<>();
                            wordsNew.add(result.toString());
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
        SendMessage incorrectMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(massage);
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
