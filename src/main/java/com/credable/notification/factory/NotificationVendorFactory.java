package com.credable.notification.factory;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.credable.notification.dto.VendorDTO;
import com.credable.notification.service.NotificationVendorService;

@Component
public class NotificationVendorFactory {
    @Autowired
    private Map<String,NotificationVendorService> notificationVendorServiceMap;

    public NotificationVendorService getNotificationType(VendorDTO vendorDTO){
        return notificationVendorServiceMap.get(vendorDTO.getNotificationType()+"_"+vendorDTO.getVendorName());
    }
}
