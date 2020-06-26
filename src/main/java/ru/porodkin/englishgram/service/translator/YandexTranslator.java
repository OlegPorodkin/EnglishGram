package ru.porodkin.englishgram.service.translator;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class YandexTranslator {

    public String translateWord(String word) {
        String result = null;

        try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String token = getToken();

            HttpPost post = new HttpPost("https://translate.api.cloud.yandex.net/translate/v2/translate");
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "Bearer " + token);

            String json = "{\n" +
                    "   \"folder_id\": \"b1ga565ero0pohv2v3pr\",\n" +
                    "   \"texts\": [\"" + word + "\"],\n" +
                    "   \"source_language_code\": \"en\",\n" +
                    "   \"targetLanguageCode\": \"ru\"\n" +
                    "}";

            StringEntity body = new StringEntity(json, ContentType.APPLICATION_JSON);

            post.setEntity(body);

            final HttpClientResponseHandler<String> responseHandler = response -> {
                final int status = response.getCode();
                if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
                    final HttpEntity entity = response.getEntity();
                    try {
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } catch (final ParseException ex) {
                        throw new ClientProtocolException(ex);
                    }
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            result = httpClient.execute(post, responseHandler);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getToken() {
        Process exec = null;
        String consoleLine = null;
        try {
            exec = Runtime.getRuntime().exec("yc iam create-token");
            InputStream inputStream = exec.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            consoleLine = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return consoleLine;
    }
}
