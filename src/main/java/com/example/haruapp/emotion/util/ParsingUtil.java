package com.example.haruapp.emotion.util;

import com.example.haruapp.global.error.CustomException;
import com.example.haruapp.global.error.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class ParsingUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Map<String, Double>> parseSim(String json) {
        try {
            return objectMapper.readValue(
                    json,
                    new TypeReference<>() {}
            );
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JSON_CONVERT_FAILED);
        }
    }
}
