package com.credable.notification.service.impl;

import com.credable.notification.Utils.CryptoUtils;
import com.credable.notification.components.ApplicationProperties;
import com.credable.notification.constants.NotificationConstant;
import com.credable.notification.constants.NotificationStatus;
import com.credable.notification.constants.NotificationType;
import com.credable.notification.constants.VendorName;
import com.credable.notification.dao.NotificationDAO;
import com.credable.notification.dto.NotificationDTO;
import com.credable.notification.dto.NotificationDataDTO;
import com.credable.notification.dto.NotificationResponseDTO;
import com.credable.notification.dto.VendorDTO;
import com.credable.notification.factory.NotificationVendorFactory;
import com.credable.notification.model.Notification;
import com.credable.notification.model.NotificationLog;
import com.credable.notification.model.Product;
import com.credable.notification.model.ProductVendor;
import com.credable.notification.repository.NotificationRepository;
import com.credable.notification.repository.ProductRepository;
import com.credable.notification.repository.ProductVendorDAO;
import com.credable.notification.service.NotificationHelperService;
import com.credable.notification.service.NotificationService;
import com.credable.notification.service.NotificationVendorService;
import com.credable.notification.service.SchedulerService;
import com.credable.notification.service.impl.vendors.TwilioNotification;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Log4j2
public class MailScheduler implements SchedulerService {

    @Autowired
    NotificationHelperService notificationHelperService;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private NotificationDAO notificationDAO;

    @Autowired
    private LineChart lineChart;
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVendorDAO productVendorDAO;
    
    @Autowired
    private NotificationVendorFactory notificationVendorFactory;

    @Async("notificationThread")
    @Override
    public void scheduleMail() {
        LocalDateTime now = LocalDateTime.now();
        List<Notification> pendingNotifications = notificationRepository.findAllPendingNotifications(applicationProperties.getMaxRetryCount(), now);
        
        List<NotificationLog> notificationLogList = new ArrayList<>();
        log.info("Attempting to send {} notifications", pendingNotifications.size());
        notificationHelperService.markAsInprogress(pendingNotifications);

        for (Notification pendingNotification : pendingNotifications) {
        NotificationLog notificationLog = new NotificationLog();
            pendingNotification.setRetryCount(pendingNotification.getRetryCount() + 1);
            try {
                NotificationResponseDTO vendorResponse = notificationService.triggerNotification(pendingNotification);
                if (vendorResponse != null) {
                    notificationLog.setRequest(vendorResponse.getRequest());
                    notificationLog.setResponse(vendorResponse.getResponse());
                    notificationLog.setReferenceId(vendorResponse.getReferenceId());
                    notificationLog.setNotification(pendingNotification);
                    if (vendorResponse.getResult() == Boolean.TRUE)
                        pendingNotification.setStatus(NotificationStatus.SENT);
                    else
                        pendingNotification.setStatus(NotificationStatus.FAILED);
                } else {
                    notificationLog.setResponse("Response from vendor service null");
                    pendingNotification.setStatus(NotificationStatus.FAILED);
                }
            } catch (Exception e) {
                log.error("Exception while triggering notification ", e);
                notificationLog.setResponse(ExceptionUtils.getStackTrace(e));
                pendingNotification.setStatus(NotificationStatus.FAILED);
            }
            notificationLogList.add(notificationLog);
        }
        notificationHelperService.saveInNewTransaction(pendingNotifications);
        notificationHelperService.saveNotificationLogs(notificationLogList);
    }

    @Override
    public void scheduleNotificationData() throws Exception {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime pastDate = LocalDate.now().minusDays(6).atStartOfDay();
        List<NotificationDataDTO> notificationDataDTOList = notificationDAO.fetchNotificationData(currentDate, pastDate);
        String subject = String.format(NotificationConstant.NOTIFICATION_DATA_EMAIL_SUBJECT, pastDate.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")), currentDate.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
        if (notificationDataDTOList.isEmpty())
            log.info("No notifications sent");

        else {
            notificationDataDTOList = addMissingDates(notificationDataDTOList, pastDate, currentDate);
            String image = CryptoUtils.encodeToBase64(lineChart.notificationCount(notificationDataDTOList));
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setRecipient(applicationProperties.getEmailReceivers());
            notificationDTO.setSubject(subject);
            notificationDTO.setNotificationType(NotificationType.EMAIL);
            Product product = productRepository.findByProductKey(applicationProperties.getProductKeyInternal());
            ProductVendor pv = productVendorDAO.findByProductAndType(product.getId(), NotificationType.EMAIL);
            VendorDTO vendorDTO = notificationHelperService.convertToVendorDTO(pv.getVendor());
            NotificationVendorService vendorService = notificationVendorFactory.getNotificationType(vendorDTO);
            String response = emailIntro(notificationDataDTOList, image, vendorDTO.getVendorName());
            notificationDTO.setBody(response);
            Map<String, String> metadata = new HashMap<>();
            metadata.put("image", image);
            notificationDTO.setMetadata(metadata);
            vendorService.sendNotification(notificationDTO, vendorDTO);
        }
    }

    private List<NotificationDataDTO> addMissingDates(List<NotificationDataDTO> notificationDataDTOList, LocalDateTime pastDate, LocalDateTime currentDate) {

        Map<LocalDate, NotificationDataDTO> availableDates = notificationDataDTOList.stream().collect(Collectors.toMap(NotificationDataDTO::getDate, Function.identity()));

        List<NotificationDataDTO> finalDates = new ArrayList<>();
        for (LocalDate d = pastDate.toLocalDate(); !d.isAfter(currentDate.toLocalDate()); d = d.plusDays(1)) {
            if (availableDates.containsKey(d)) {
                finalDates.add(availableDates.get(d));
            } else {
                NotificationDataDTO dto = new NotificationDataDTO();
                dto.setDate(d);
                dto.setEmailCount(0L);
                dto.setSmsCount(0L);
                dto.setWhatsappCount(0L);
                finalDates.add(dto);
            }
        }
        return finalDates;
    }

    private String emailIntro(List<NotificationDataDTO> notificationDataDTOList, String image, VendorName name) throws IOException {
        StringBuilder result = new StringBuilder();
        result.append("<html>");
        result.append("<head>");
        result.append("<title></title>");
        result.append("</head>");
        result.append("<body>");
        result.append("<p>Dear Sir / Ma'am,</p>");
        result.append("<p>Please find the details of notifications sent in 1 week").append("</p>");
        if(name == VendorName.SENDGRID) 
            result.append("<img width=\"96%\" src=\"data:image/jpeg;base64," + image + " encoding\"/>");
        else 
            result.append("<img width='96%' src='cid:chart_image'");
         
        result.append("<br>");
        result.append("<p></p>");
        result.append("<br>");
        result.append("<table style=\"border:1px solid black; margin-left:auto; margin-right:auto; border-collapse: collapse;\" width=\"92%\">");
        result.append(createTableRow(notificationDataDTOList));
        result.append("</table> ");
        result.append("</body>");
        result.append("</html>");
        return result.toString();
    }

    private String createTableRow(List<NotificationDataDTO> notificationDataDTOList) {
        StringBuilder result = new StringBuilder();
        result.append("<tr>").append("<td style=\"border:1px solid black; width: 18%; border-collapse: collapse; padding:5px;\"><b>").append("Date").append("</b></td>");
        result.append("<td style=\"border:1px solid black; border-collapse: collapse; width: 10%; padding:5px;\"><b>").append("SMS").append("</b></td>")
                .append("<td style=\"border:1px solid black; border-collapse: collapse; width: 10%; padding:5px;\"><b>").append("Email").append("</b></td>")
                .append("<td style=\"border:1px solid black; border-collapse: collapse; width: 10%; padding:5px;\"><b>").append("Whatsapp").append("</b></td>")
                .append("</tr>");

        for (NotificationDataDTO notificationDataDTO : notificationDataDTOList) {
            result.append("<tr>").append("<td style=\"border:1px solid black; border-collapse: collapse; padding:5px;\">").append(notificationDataDTO.getDate().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))).append("</td>");
            result.append("<td style=\"border:1px solid black; border-collapse: collapse; padding:5px;\">").append(notificationDataDTO.getSmsCount()).append("</td>")
                    .append("<td style=\"border:1px solid black; border-collapse: collapse; padding:5px;\">").append(notificationDataDTO.getEmailCount()).append("</td>")
                    .append("<td style=\"border:1px solid black; border-collapse: collapse; padding:5px;\">").append(notificationDataDTO.getWhatsappCount()).append("</tr>");
        }
        return result.toString();
    }

}
