package com.example.actors.wordcount.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UtilityMapper {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
