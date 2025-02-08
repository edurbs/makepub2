package com.github.edurbs.makepub2.app.utils.requestapi;

import okhttp3.*;
import org.springframework.stereotype.Component;

import com.github.edurbs.makepub2.app.gateway.RequestApi;
import com.github.edurbs.makepub2.app.usecase.exceptions.UseCaseException;

import java.io.IOException;

@Component
public class OkHttpRequestApi implements RequestApi {
    @Override
    
    public String get( String apiUrl,  String textToConvert) throws UseCaseException {
        if(textToConvert.isBlank()) {
            return "";
        }
        String requestBody = """
                {
                    "text" : "%s"
                }
                """.formatted(textToConvert);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(RequestBody.create(requestBody, MediaType.get("application/json")))
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            throw new UseCaseException("Erro ao consultar a API: " + apiUrl, e.getCause());
        }
        return textToConvert;
    }
}
