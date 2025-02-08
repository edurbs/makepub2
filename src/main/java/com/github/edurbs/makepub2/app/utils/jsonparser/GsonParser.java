package com.github.edurbs.makepub2.app.utils.jsonparser;

import com.github.edurbs.makepub2.app.gateway.JsonParser;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GsonParser<T> implements JsonParser<T>  {
    @Override
    
    public List<T> parse( String json,  Class<T[]> clazz) {
        if(json.isBlank()) {
            return List.of();
        }
        try {
            T[] array = new Gson().fromJson(json, clazz);
            return List.of(array);
        } catch (Exception e) {
            return List.of();
        }
    }
}
