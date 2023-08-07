package com.credable.notification.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.credable.notification.Utils.converters.MapOfStringConverter;
import com.credable.notification.constants.NotificationCategory;
import com.credable.notification.constants.NotificationStatus;
import com.credable.notification.constants.NotificationType;

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
@Table(name = "notification")
public class Notification implements Serializable {
        private static final long serialVersionUID = -1447585156625212640L;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "product_id", referencedColumnName = "id")
        private Product product;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "vendor_id", referencedColumnName = "id")
        private Vendor vendor;

        @NonNull
        @Column(name = "type")
        @Enumerated(EnumType.STRING)
        private NotificationType type;
        
        @NonNull
        @Column(name = "category")
        @Enumerated(EnumType.STRING)
        private NotificationCategory category;
        
        @NonNull
        @Column(name = "recipient")
        private String recipient;

        @Column(name = "email_cc")
        private String emailCC;

        @Column(name = "email_bcc")
        private String emailBcc;

        @Column(name = "subject")
        private String subject;

        @Column(name = "body")
        private String body;

        @Column(name = "response")
        private String response;

        @Column(name = "request")
        private String request;
        
        @Column(name = "reference_id")
        private String referenceId;
        
        @Enumerated(EnumType.STRING)
        @NonNull
        @Column(name = "status")
        private NotificationStatus status;

        @Column(name = "retry_count")
        private Integer retryCount;

        @Column(name = "scheduled_time")
        private LocalDateTime scheduledTime;

        @Column(name = "created")
        private LocalDateTime created;

        @Column(name = "updated")
        private LocalDateTime updated;

        @Column(name = "vendor_status")
        private String vendorStatus;
        
        @Column(name="metadata")
        @Convert(converter=MapOfStringConverter.class)
        private Map<String,String> metadata;

        @OneToMany(fetch = FetchType.LAZY, mappedBy="notification")
        private List<NotificationAttachment> notificationAttachments;

}
