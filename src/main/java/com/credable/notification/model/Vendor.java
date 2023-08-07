package com.credable.notification.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.credable.notification.Utils.converters.MapOfStringConverter;
import com.credable.notification.constants.NotificationType;
import com.credable.notification.constants.Status;
import com.credable.notification.constants.VendorName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vendor")
public class Vendor implements Serializable {

    private static final long serialVersionUID = -1447585156625212640L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "vendor_type")
    @Enumerated(EnumType.STRING)
    private NotificationType vendorType;

    @NonNull
    @Column(name = "vendor_name")
    @Enumerated(EnumType.STRING)
    private VendorName vendorName;

    @Enumerated(EnumType.STRING)
    @NonNull
    @Column(name = "status")
    private Status status;

    @NonNull
    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "access_key")
    private String accessKey;

    @Column(name = "base_url")
    private String baseUrl;
    
    @Column(name = "uris")
    @Convert(converter = MapOfStringConverter.class)
    private Map<String,String> uris;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "updated")
    private LocalDateTime updated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="vendor") 
    private List<ProductVendor> productVendors;

}
