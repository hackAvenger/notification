package com.credable.notification.Utils.converters;

import java.io.IOException;
import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.json.JSONObject;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.credable.notification.Utils.ConverterUtils;

@Converter
public class MapOfStringConverter implements AttributeConverter<Map<String,String>, String> { 
  @Override
  public String convertToDatabaseColumn(Map<String, String> attribute) {
    if (CollectionUtils.isEmpty(attribute)) {
      return null;
    }

    return new JSONObject(attribute).toString();
  }

  @Override
  public Map<String, String> convertToEntityAttribute(String dbData) {
    if (!StringUtils.hasText(dbData)) {
      return null;
    }
    
    try {
      return ConverterUtils.convertFromStringToMapOfString(dbData);
    } catch (IOException e) {
      return null;
    }
  }
}


