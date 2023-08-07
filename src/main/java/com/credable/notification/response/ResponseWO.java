package com.credable.notification.response;

import com.credable.notification.constants.CredableResponseStatus;

import lombok.Getter;

@Getter
public class ResponseWO<T> {
  private final CredableResponseStatus status;
  private final String messageCode;
  private final String description;
  private final T data;

  protected ResponseWO(Builder<T> builder) {
    this.status = builder.status;
    this.description = builder.description;
    this.messageCode = builder.messageCode;
    this.data = builder.data;
  }

  public static <T> Builder<T> builder(Class<T> tClass) {
    return new Builder<>();
  }

  public static <T> Builder<T> okBuilder(T data) {
    Builder<T> builder = new Builder<>();
    builder.setStatus(CredableResponseStatus.OK);
    builder.setData(data);
    return builder;
  }

  public static <T> Builder<T> errorBuilder() {
    Builder<T> builder = new Builder<>();
    builder.setStatus(CredableResponseStatus.BADREQUEST);
    return builder;
  }
  
  public static class Builder<T> {
    private CredableResponseStatus status;
    private String messageCode;
    private String description;
    private T data;

    public Builder<T> setStatus(CredableResponseStatus status) {
      this.status = status;
      return this;
    }

    public Builder<T> setMessageCode(String messageCode) {
      this.messageCode = messageCode;
      return this;
    }

    public Builder<T> setDescription(String description) {
      this.description = description;
      return this;
    }

    public Builder<T> setData(T data) {
      this.data = data;
      return this;
    }
    
    public ResponseWO<T> build() {
      return new ResponseWO<>(this);
    }
  }
}
