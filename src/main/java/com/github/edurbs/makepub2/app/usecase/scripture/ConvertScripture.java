package com.github.edurbs.makepub2.app.usecase.scripture;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.github.edurbs.makepub2.app.gateway.RequestApi;

@Component
@RequiredArgsConstructor
public class ConvertScripture {

    
    private final RequestApi requestApi;

    public String execute(final String text) {
        String apiUrl = "https://conversorxv.eduardo.soares.nom.br/rest/services/convert/execute";
        return requestApi.get(apiUrl, text);
    }

}


