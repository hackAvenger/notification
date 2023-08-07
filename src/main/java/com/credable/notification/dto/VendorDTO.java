package com.credable.notification.dto;

import java.util.Map;

import com.credable.notification.model.Notification;
import com.credable.notification.model.ProductVendor;
import com.credable.notification.model.Vendor;
import org.springframework.util.StringUtils;

import com.credable.notification.constants.NotificationType;
import com.credable.notification.constants.VendorName;

import lombok.Getter;

@Getter
public class VendorDTO {
    private NotificationType notificationType;
    private VendorName vendorName;
    private String secretKey;
    private String accessKey;
    private String baseUrl;
    private Map<String,String> uris;

    private Long vendorId;

    public VendorDTO(NotificationType notificationType, VendorName vendorName) {
        this.notificationType = notificationType;
        this.vendorName = vendorName;
    }

    public VendorDTO setSecretKey(String secretKey) {
      this.secretKey = secretKey;
      return this;
    }

    public VendorDTO setAccessKey(String accessKey) {
      this.accessKey = accessKey;
      return this;
    }
    
    public VendorDTO setBaseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
      return this;
    }
    

    public VendorDTO setUris(Map<String, String> uris) {
      this.uris = uris;
      return this;
    }

    public VendorDTO setVendorId(Long vendorId) {
        this.vendorId = vendorId;
        return this;
    }

    public static VendorDTO from(Notification notification) {
      Vendor vendor = notification.getVendor();
      return new VendorDTO(notification.getType(), vendor.getVendorName()).setAccessKey(vendor.getAccessKey()).setSecretKey(vendor.getSecretKey()).setBaseUrl(vendor.getBaseUrl())
        .setUris(vendor.getUris()).setVendorId(vendor.getId());
    }

    @Override
    public String toString() {
      return "VendorDTO [notificationType=" + this.notificationType + ", vendorName=" + this.vendorName + 
          ", isSecretKeyPresent=" + StringUtils.hasText(this.secretKey) + ", isAccessKeyPresent=" + StringUtils.hasText(this.accessKey) + "]";
    }
}
