package com.github.edurbs.makepub2.app.gateway;

import java.io.Serializable;

import com.vaadin.flow.function.SerializableBiConsumer;

@FunctionalInterface
public interface MySerializableConsumer<T> extends SerializableBiConsumer<T, Serializable> {
    
}
