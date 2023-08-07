package com.credable.notification.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.credable.notification.constants.Status;

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
@Table(name = "product")
public class Product implements Serializable {

        private static final long serialVersionUID = -1447585156625212640L;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "product_name")
        @NonNull
        private String productName;

        @Column(name = "product_key")
        @NonNull
        private String productKey;

        @Enumerated(EnumType.STRING)
        @NonNull
        @Column(name = "status")
        private Status status;

        @Column(name = "created")
        private LocalDateTime created;

        @Column(name = "updated")
        private LocalDateTime updated;

        @OneToMany(fetch = FetchType.LAZY, mappedBy="product")
        private List<ProductVendor> productVendors;

}
