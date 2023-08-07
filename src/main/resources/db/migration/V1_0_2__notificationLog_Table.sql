CREATE TABLE notification_log(id bigint NOT NULL auto_increment, notification_id bigint, request TEXT, 
response TEXT, reference_id varchar(255), PRIMARY KEY (id),
FOREIGN KEY(`notification_id`) 
REFERENCES `notification`.`notification`(`id`))engine = InnoDB;