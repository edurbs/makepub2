package com.github.edurbs.makepub2.app.gateway;

import java.util.List;

public interface JsonParser <T> {
    List<T> parse(String json, Class<T[]> clazz);
}
