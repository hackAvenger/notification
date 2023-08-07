package com.credable.notification.Utils;

import java.util.Collections;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class RestClient {

  private RestTemplate restTemplate;

  private ObjectMapper objectMapper;

    public RestClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
      this.restTemplate = restTemplate;
      this.objectMapper = objectMapper;
    }

  public <T> Optional<T> post(String url, Object request, TypeReference<T> typeReference) {
    try {
      log.info("Calling Url {} Request Method {} request body {}", url, "POST", request);
      ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
      log.info("Response from Url {} is {}", url, response);
      if (response.getStatusCode().equals(HttpStatus.OK)) {
        if (StringUtils.hasText(response.getBody())) {
          return Optional.of(objectMapper.readValue(response.getBody(), typeReference));
        } else
          return Optional.empty();
      } else {
        return Optional.empty();
      }
    } catch (Exception ex) {
      log.error("Exception while calling URL {}", url, ex);
      return Optional.empty();
    }
  }

  public <T> Optional<T> put(String url, Object request, Class<T> classOfT) {
    try {
      log.info("Calling Url {} Request Method {} request body {}", url, "PUT", request);

      ResponseEntity<String> response = putForEntity(url, request, String.class);
      log.info("Response from Url {} is {}", url, response);
      if (response.getStatusCode().equals(HttpStatus.OK)) {
        if (StringUtils.hasText(response.getBody())) {
          return Optional.of(objectMapper.readValue(response.getBody(), classOfT));
        } else
          return Optional.empty();
      } else {
        return Optional.empty();
      }
    } catch (Exception ex) {
      log.error("Exception while calling URL {}", url, ex);
      return Optional.empty();
    }
  }
  
  public byte[] getForByte(String url) {
    try {
      log.info("Calling Url {} Request Method {}", url, "GET");
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
      HttpEntity<String> entity = new HttpEntity<>(headers);
      ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
      log.info("Response from Url {} is {}", url, response);
      if (response.getStatusCode().equals(HttpStatus.OK)) {
        if(response.hasBody())
          return response.getBody();
      } else {
        return null;
      }
    } catch (Exception ex) {
      log.error("Exception while calling URL {}", url, ex);
      return null;
    }
    return null;
  }


  public <T> Optional<T> get(String url, Class<T> classOfT) {
    try {
      log.info("Calling Url {} Request Method {}", url, "GET");
      ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
      log.info("Response from Url {} is {}", url, response);
      if (response.getStatusCode().equals(HttpStatus.OK)) {
        if (StringUtils.hasText(response.getBody())) {
          return Optional.of(objectMapper.readValue(response.getBody(), classOfT));
        } else
          return Optional.empty();
      } else {
        return Optional.empty();
      }
    } catch (Exception ex) {
      log.error("Exception while calling URL {}", url, ex);
      return Optional.empty();
    }
  }

  public <T> Optional<T> get(String url, HttpEntity<Object> request, Class<T> classOfT) {
    try {
      log.info("Calling Url {} Request Method {} request body {}", url, "GET", request);
      ResponseEntity<String> response = restTemplate.exchange(url,HttpMethod.GET ,request, String.class);

      if (response.getStatusCode().equals(HttpStatus.OK)) {
        if (StringUtils.hasText(response.getBody())) {
          return Optional.of(objectMapper.readValue(response.getBody(), classOfT));
        } else
          return Optional.empty();
      } else {
        return Optional.empty();
      }
    } catch (Exception ex) {
      log.error("Exception while calling URL {}", url, ex);
      return Optional.empty();
    }
  }

  private  <T> ResponseEntity<T> putForEntity(String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {

    RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
    ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
    return nonNull(restTemplate.execute(url, HttpMethod.PUT, requestCallback, responseExtractor, uriVariables));
  }

  private static <T> T nonNull(@Nullable T result) {
    Assert.state(result != null, "No result");
    return result;
  }


}
