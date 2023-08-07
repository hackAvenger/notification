create table notification (
    id bigint NOT NULL auto_increment,
    created datetime(6) NOT NULL,
    updated datetime(6) NOT NULL,
    recipient varchar(255) NOT NULL,
    body TEXT NOT NULL,
    subject TEXT,
    email_cc varchar(255),
    email_bcc varchar(255),
    request TEXT,
    response TEXT,
    reference_id varchar(255),
    retry_count integer default 0,
    scheduled_time datetime(6),
    metadata TEXT,
    category ENUM('OTP', 'TRANSACTION', 'NOT_APPLICABLE') DEFAULT 'NOT_APPLICABLE',
    type ENUM('SMS','EMAIL','WHATSAPP') DEFAULT NULL,
    status ENUM('DELIVERED', 'SENT', 'PENDING', 'FAILED', 'INPROGRESS') DEFAULT NULL,
    product_id bigint,
    vendor_id bigint,
    primary key (id)
) engine = InnoDB;

create table notification_attachment (
    id bigint NOT NULL auto_increment,
    created datetime(6) NOT NULL,
    updated datetime(6) NOT NULL,
    file_name varchar(255),
    file_url varchar(255) NOT NULL,
    notification_id bigint,
    upload_type ENUM('S3_LINK', 'FILE') DEFAULT NULL,
    primary key (id)
) engine = InnoDB;

create table product (
    id bigint NOT NULL auto_increment,
    created datetime(6) NOT NULL,
    updated datetime(6) NOT NULL,
    product_key varchar(255),
    product_name varchar(255),
    status ENUM('ACTIVE','INACTIVE','DELETED') DEFAULT NULL,
    primary key (id)
) engine = InnoDB;

create table product_vendor (
    id bigint NOT NULL auto_increment,
    created datetime(6) NOT NULL,
    updated datetime(6) NOT NULL,
    sender VARCHAR(200),
    from_name VARCHAR(100),
    status ENUM('ACTIVE','INACTIVE','DELETED') DEFAULT NULL,
    product_id bigint,
    vendor_id bigint,
    primary key (id)
) engine = InnoDB;

create table vendor (
    id bigint NOT NULL auto_increment,
    created datetime(6) NOT NULL,
    updated datetime(6) NOT NULL,
    access_key TEXT,
    secret_key TEXT,
    base_url varchar(255),
    uris varchar(255),
    vendor_name varchar(255),
    vendor_type varchar(255),
    status ENUM('ACTIVE','INACTIVE','DELETED') DEFAULT NULL,
    primary key (id)
) engine = InnoDB;

alter table notification add constraint FKbv8kwwu9ebnqglt5lj6mf8gsm foreign key (product_id) references product (id);

alter table notification add constraint FK7usgy1efav68q2dfbjnf7a870 foreign key (vendor_id) references vendor (id);

alter table notification_attachment add constraint FK23ehk4v86waa6oi9fg2l9t16r foreign key (notification_id) references notification (id);

alter table product_vendor add constraint FK60ssua8d4oysje24ijp9edkyt foreign key (product_id) references product (id);

alter table product_vendor add constraint FK4dh5569om6a0g8xvmke9icrww foreign key (vendor_id) references vendor (id);
