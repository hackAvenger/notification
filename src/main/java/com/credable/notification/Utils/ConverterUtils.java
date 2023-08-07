package com.credable.notification.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

@Getter
public class ConverterUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private ConverterUtils() {}

  public static Map<String, List<String>> convertFromStringToMapOfListString(String dbData) throws IOException {
    if (!StringUtils.hasText(dbData))
      return new HashMap<>();
    TypeReference<Map<String, List<String>>> typeReference = new TypeReference<Map<String, List<String>>>() {};
    return objectMapper.readValue(dbData, typeReference);
  }

  public static <T> T convertFromMapToDTO(Map<String, List<String>> map, Class<T> classOfT) {
    return objectMapper.convertValue(map, classOfT);
  }

  public static Map<String, String> convertFromStringToMapOfString(String dbData) throws IOException {
    if (!StringUtils.hasText(dbData))
      return new HashMap<>();
    TypeReference<Map<String, String>> typeReference = new TypeReference<Map<String, String>>() {};
    return objectMapper.readValue(dbData, typeReference);
  }

  public static List<String> convertFromStringToList(String dbData) throws IOException {
    TypeReference<List<String>> typeReference = new TypeReference<List<String>>() {};
    return objectMapper.readValue(dbData, typeReference);
  }
}
