INSERT INTO `notification`.`product` (`created`, `product_key`, `product_name`, `status`, `updated`) VALUES (now(), '?', 'UpScale', 'ACTIVE', now());
INSERT INTO `notification`.`vendor` (`access_key`, `base_url`, `created`, `secret_key`, `status`, `updated`, `uris`, `vendor_name`, `vendor_type`) VALUES ('upscaleissuecred@gmail.com', '', NOW(), 'ovohpxkzuhvjkbri', 'ACTIVE', NOW(), '', 'GMAIL', 'EMAIL');
INSERT INTO `notification`.`vendor` (`access_key`, `base_url`, `created`, `status`, `updated`, `uris`, `vendor_name`, `vendor_type`) VALUES ('?', 'https://api.in.freshchat.com', NOW(), 'ACTIVE', NOW(), '{\n    \"freshchatOutboundUri\": \"/v2/outbound-messages/whatsapp\"\n}', 'FRESHCHAT', 'WHATSAPP');
INSERT INTO `notification`.`vendor` (`access_key`, `base_url`, `created`, `status`, `updated`, `uris`, `vendor_name`, `vendor_type`) VALUES ('?', '', NOW(), 'ACTIVE', NOW(), '', 'SENDGRID', 'EMAIL');
INSERT INTO `notification`.`vendor` (`access_key`, `base_url`, `created`, `status`, `updated`, `uris`, `vendor_name`, `vendor_type`) VALUES ('?', 'https://api.msg91.com/api/v5/', NOW(), 'ACTIVE', NOW(), '{
    "msg91OtpUri" : "otp",
    "msg91TransactionUri" : "flow/"
}', 'MSG91', 'SMS');
SELECT @sendgrid_id:=id FROM vendor where vendor_name='SENDGRID';
INSERT INTO `notification`.`product_vendor` (`created`, `status`, `updated`, `product_id`, `vendor_id`, `from_name`, `sender`) VALUES (NOW(), 'ACTIVE', NOW(), '1', @sendgrid_id, 'noreply@upscalemail.cash', 'UPSCALE');
SELECT @gmail_id:=id FROM vendor where vendor_name='GMAIL';
INSERT INTO `notification`.`product_vendor` (`created`, `status`, `updated`, `product_id`, `vendor_id`, `from_name`, `sender`) VALUES (NOW(), 'ACTIVE', NOW(), '1', @gmail_id, '', '');
SELECT @msg91_id:=id FROM vendor where vendor_name='MSG91';
INSERT INTO `notification`.`product_vendor` (`created`, `status`, `updated`, `product_id`, `vendor_id`, `from_name`, `sender`) VALUES (NOW(), 'ACTIVE', NOW(), '1', @msg91_id, '', 'UPSCLE');
SELECT @freshchat_id:=id FROM vendor where vendor_name='FRESHCHAT';
INSERT INTO `notification`.`product_vendor` (`created`, `status`, `updated`, `product_id`, `vendor_id`, `from_name`, `sender`) VALUES (NOW(), 'ACTIVE', NOW(), '1', @freshchat_id, '9372783043', '');


update product_vendor pv JOIN vendor v ON pv.vendor_id=v.id SET `sender` = '9372783043', `from_name` = '' where v.vendor_name = 'FRESHCHAT';
update product_vendor pv JOIN vendor v ON pv.vendor_id=v.id SET `sender` = '', `from_name` = 'UPSCLE' where v.vendor_name = 'MSG91';
update product_vendor pv JOIN vendor v ON pv.vendor_id=v.id SET `sender` = 'noreply@upscalemail.cash', `from_name` = 'UPSCALE' where v.vendor_name = 'SENDGRID';


INSERT INTO `product` (`created`, `product_key`, `product_name`, `status`, `updated`) VALUES (NOW(), 'NOTIFICATION_INTERNAL', 'NOTIFICATION INTERNAL', 'ACTIVE', NOW());
select @internal_id:=id from product where product_key='NOTIFICATION_INTERNAL';
select @sendgrid_id:=id from vendor where vendor_name='SENDGRID';
insert into product_vendor(`created`,`updated`,`status`,`product_id`,`vendor_id`,`from_name`,`sender`) VALUES(NOW(),NOW(),'ACTIVE',@internal_id,@sendgrid_id,'noreply@upscalemail.cash','UPSCALE');


-- UPS-3504
update vendor set uris = '{\n    \"freshchatOutboundUri\": \"/v2/outbound-messages/whatsapp\", \"freshchatStatusUri\":\"/v2/outbound-messages?request_id=\" \n}' where vendor_name = 'FRESHCHAT' and vendor_type = 'WHATSAPP';

INSERT INTO `notification_log` (`notification_id`,`request`,`response`,`reference_id`) SELECT id, request, response, reference_id FROM notification;

-- UPS-3627 29-Sept
INSERT INTO `notification`.`vendor` (`access_key`, `created`, `status`, `updated`, `vendor_name`, `vendor_type`) VALUES ('{\n  \"type\": \"service_account\",\n  \"project_id\": \"upscale-test\",\n  \"private_key_id\": \"72a855d443eaeb67cc52027a2a506aab879c0b6b\",\n  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDFgAc80bRtsk9y\\nkqMMVo1hwws/bASb/yqVib5Yj9ZE3iJQAwkEKvpshlXlZWBSUG0nNUwkNHboxfQ8\\ntafm0L4zGKkdonmqp+wMD9TNLH8abj/q3rEhIqbj5JYGIUMMjxryYdJoOADdm9CS\\n9jZS1efDm3ZY5EyYGh6q1GXUxveLEKmiS+kqriU/8l4rF15mksHQspfkFL1SDjLl\\nogNe/6TPiEytGvHisXwLk1Oi853fxQRdwsjA7lH05SDp7w33s8phpzt5oXA54Rk0\\nxMJyZlRQJ7RbJSs2+cNaVwWLDAsSgB48rE/MVyJD+x+eldqN1lj/cT8fzswqsFcU\\nyUp7p3jFAgMBAAECggEAJDb7/MPCRzwjU9G+KqDDV4oVialjtfwl4ZpKqEAlzOaD\\nmbLYyCYeWeCPJxt9/FIJld8wyVHCfR/xT2GK+mjL+6AZwJieoTOd9b9AaOfwHZda\\nSpXPmpEZU6ACf3jBa+/gw9G6rIgaZohwCEa0At/82dIrsxSX0+s0NORyEMB0O1A9\\ntKCWXwEYcY6C7BvEje6Z7JGH+so5sInUtYYOrlceIv1QDTp+zAhWb2IocCUbIy/O\\nHoqQ5RmxZP39Krj6azs4C5Q+wbqYmZAF6kSxSZ8CfHCD/EPY27QwYu9obl2imhrj\\nDAwxW+i9o7amxMGdzfbOo2Eult/O+NNduYle4g5fMQKBgQD5p7sQ4m+wIc989ebA\\nHQe5l5U6ypFkqpaA6ux780XIGpzyesq0JyJUak+Yey3ZU+ubs+xRXC4e9AX3i5hq\\nMCIWOWPD7P4u14HbqnPQxguoF665GDUj09uPCjCcFPETY0/k9xjm8A5kueLqZb9H\\n5LOqxyz59nAow9VhZtt6n65nsQKBgQDKhPlS87Bb3uqEvaVM6XhJ7tq6TuNSHoiG\\nQNe0b4RRealfI0yilvloeH6Zfvatl3n3V3Nucr1NWzKckFEb05dzF6r5m7khsW82\\na0KGt2CZEg71oa9bRVX2V0JF/0WnTHuFLhhZ5gMUWVTv3iDcBoeNUbCh5B9AAXwH\\nRa6egf17VQKBgFAmOThCefjh4esMRviGWWSL9azaO7NF0OuygBmLEGwdnyV5pnoP\\n1kWjm8ooQZ/0AUALk2LLZUMqtH+0oUn2Cft4kT7lZvVC2r3bCzOLYVKlx8JEs0q9\\nSU+yxchWMM84ov2vFp+NkhFkXCVEZRR/p+674wScyYVM2cRbcTr86NEBAoGBAJ8Z\\nhD3U0DH8mEUcL+aSbDu4+ADxxMhY0GyYOF26Vjsj1xm0w5nf/aRDNUIlamnJ3Lmh\\n7D9SyMEi6knxXqb7vblfWKSr3+9GU9ZuwV7QZYUbmMED+p07hWL3/kq7Vt8tyMRC\\nCENn3fd4jq+61BnGz2sISDmG274O7H9SEPLZsGfZAoGBAOr3E/Sb9XqJsCf2KyNU\\nWJCHCtAZFjybyzKw+t5F3wnjh+Jx/2E1trvNFg4UOZswcnxPtgl4P1ep7cHYWhac\\nbFs0VPhIQBCShxSREwXj4RxFZACTM3lESssE+FmKAj6UEio9sGHstFCox+kP2MpP\\nOty0FHnjEF/ChcudmRIB6VMi\\n-----END PRIVATE KEY-----\\n\",\n  \"client_email\": \"firebase-adminsdk-vnqh1@upscale-test.iam.gserviceaccount.com\",\n  \"client_id\": \"104573403524854954066\",\n  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-vnqh1%40upscale-test.iam.gserviceaccount.com\"\n}', NOW(), 'ACTIVE', NOW(), 'FIREBASE', 'PUSH');

SELECT @firebase_id:=id FROM vendor where vendor_name='FIREBASE';
INSERT INTO `notification`.`product_vendor` (`created`, `status`, `updated`, `product_id`, `vendor_id`) VALUES (NOW(), 'ACTIVE', NOW(), '1', @firebase_id);

--06-october
update product_vendor set from_name='UPSCALE',sender='noreply@upscalemail.cash' where id=5;