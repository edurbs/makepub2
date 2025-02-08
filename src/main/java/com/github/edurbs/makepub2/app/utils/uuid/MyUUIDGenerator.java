package com.github.edurbs.makepub2.app.utils.uuid;

import org.springframework.stereotype.Component;

import com.github.edurbs.makepub2.app.gateway.UUIDGenerator;

import java.util.UUID;

@Component
public class MyUUIDGenerator implements UUIDGenerator {

    public String generate() {
        return UUID.randomUUID().toString();
    }
}
