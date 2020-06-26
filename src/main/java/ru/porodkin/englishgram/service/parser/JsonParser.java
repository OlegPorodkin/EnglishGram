package ru.porodkin.englishgram.service.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> extractDictionary(String dictionary) {

        List<String> result = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();
        Object parse = null;
        try {
            parse = jsonParser.parse(dictionary);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject dictionaryWord = (JSONObject) parse;
        JSONArray def = (JSONArray) dictionaryWord.get("def");

        def.stream()
                .map(d -> ((JSONArray) ((JSONObject) d).get("tr")).stream()
                        .map(tr -> ((JSONObject) tr).get("text"))
                        .collect(Collectors.toList()))
                .forEach(e -> result.addAll((List) e));

        return result;
    }
}
