package ru.porodkin.englishgram.service.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
public class JsonParser {

    public String extractText(String json) {
        JSONParser jsonParser = new JSONParser();
        Object parse = null;
        try {
            parse = jsonParser.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject translate = (JSONObject) parse;
        JSONArray translations = (JSONArray) translate.get("translations");
        JSONObject text = (JSONObject) translations.get(0);
        Object result = text.get("text");
        return result.toString();
    }
}
