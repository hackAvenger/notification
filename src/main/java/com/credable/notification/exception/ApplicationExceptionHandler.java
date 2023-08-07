package com.credable.notification.exception;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.credable.notification.response.ResponseWO;

import lombok.extern.log4j.Log4j2;

@RestControllerAdvice
@Log4j2
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

  public static final String INTERNAL_SERVER_ERROR = "Something went wrong! Please contact system administrator.";

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", new Date());
    body.put("status", status.value());

    List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());

    body.put("errors", errors);

    return new ResponseEntity<>(body, headers, status);

  }

  @ExceptionHandler(CredableException.class)
  @ResponseBody
  public final ResponseEntity<ResponseWO<Object>> handleApplicationException(CredableException ex) {
    return ResponseEntity.ok(ResponseWO.errorBuilder().setDescription(ex.getMessageDesc()).setMessageCode(ex.getMessageCode()).build());
  }

  @ExceptionHandler(Exception.class)
  @ResponseBody
  protected ResponseEntity<ResponseWO<Object>> handleAPIException(Exception ex) {
    log.error("################## Unhandled Exception ####################", ex);
    return ResponseEntity.ok(ResponseWO.errorBuilder().setDescription(INTERNAL_SERVER_ERROR).setMessageCode(INTERNAL_SERVER_ERROR).build());
  }
  
  @ExceptionHandler(UnAuthorizedSessionException.class)
  @ResponseBody
  public final ResponseEntity<ResponseWO<Object>> handleUnAuthorizedSessionException(UnAuthorizedSessionException ex) {
    return ResponseEntity.status(401).body(ResponseWO.errorBuilder().setDescription(ex.getMessageDesc()).setMessageCode(ex.getMessageCode()).build());
  }

}
