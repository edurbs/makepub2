package com.github.edurbs.makepub2.app.gateway;

import com.github.edurbs.makepub2.app.usecase.exceptions.UseCaseException;

public interface RequestApi {
    String get(String apiUrl, String textToConvert) throws UseCaseException;
}
