package com.levdevs.freindshipbe.Service;

import com.levdevs.freindshipbe.Entity.Guest;
import com.levdevs.freindshipbe.Entity.Reservation;
import com.levdevs.freindshipbe.enums.ReservationStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;



@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendReservationConfirmationEmailTymleaf(Reservation reservation)  {
        String recipientEmail = reservation.getGuests().get(0).getEmail();

        // Create the Thymeleaf context and add variables
        Context context = new Context();
        context.setVariable("reservation", reservation);
        context.setVariable("guest", reservation.getGuests().get(0));

        // Process the Thymeleaf template
        String emailBody = templateEngine.process("reservation_confirmation2", context);

        // Create the email message
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(recipientEmail);
            helper.setSubject("Reservation Confirmed");
            helper.setText(emailBody, true); // true for HTML content
        } catch (MessagingException e) {
            logger.info("Error sending email: " + e.getMessage());
            throw new RuntimeException(e);
        }
        // Send the email
        mailSender.send(mimeMessage);
    }

    public void sendEmailHtml(String recipientEmail, String subject, String emailBody)  {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(emailBody, true);  // Set the second parameter to true to send HTML content
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.info("Error sending email: " + e.getMessage());
            logger.error("Error sending email: " + e.getMessage());
            throw new RuntimeException("Error sending email: " + e.getMessage());
        }
    }

    public void sendStatusUpdateEmail(Reservation reservation, ReservationStatus status) {
        String recipientEmail = reservation.getGuests().get(0).getEmail();
//        String emailBody = String.format(
//                "Dear %s,\n\nYour reservation status has been updated to: %s.\n\nThank you!",
//                reservation.getGuests().get(0).getFirstName(),
//                status
//        );

        // HTML email body with styling
        String emailBody = String.format(
                "<html>" +
                        "<body>" +
                        "<h2 style='color:#4CAF50;'>Dear %s,</h2>" +
                        "<p>Your reservation status has been updated to: <strong>%s</strong>.</p>" +
                        "<p>Thank you!</p>" +
                        "<h3>Reservation Details:</h3>" +
                        "<table style='border-collapse: collapse; width: 100%%;'>" +
                        "<tr>" +
                        "<th style='border: 1px solid #ddd; padding: 8px;'>Field</th>" +
                        "<th style='border: 1px solid #ddd; padding: 8px;'>Details</th>" +
                        "</tr>" +
                        "<tr>" +
                        "<td style='border: 1px solid #ddd; padding: 8px;'>Reservation ID</td>" +
                        "<td style='border: 1px solid #ddd; padding: 8px;'>%d</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td style='border: 1px solid #ddd; padding: 8px;'>Location</td>" +
                        "<td style='border: 1px solid #ddd; padding: 8px;'>%s</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td style='border: 1px solid #ddd; padding: 8px;'>Patient</td>" +
                        "<td style='border: 1px solid #ddd; padding: 8px;'>%s %s</td>" +
                        "</tr>" +
                        "</table>" +
                        "</body>" +
                        "</html>",
                reservation.getGuests().get(0).getFirstName(),
                status,
                reservation.getId(),
                reservation.getLocation().getName(),
                reservation.getPatient().getFirstName(),
                reservation.getPatient().getLastName()
        );
        sendEmailHtml(recipientEmail, "Reservation Status Updated", emailBody);
    }

//    private void sendTheEmail(String recipientEmail, String subject, String emailBody) {
//        try {
//            sendEmail(recipientEmail, subject, emailBody);
//        } catch (Exception e) {
//            logger.info("Error sending email: " + e.getMessage());
//            logger.error("Error sending email: " + e.getMessage());
//            throw new RuntimeException("Error sending email: " + e.getMessage());
//        }
//        logger.info("Email sent successfully to: " + recipientEmail);
//    }

//    public void sendReservationConfirmationEmail(Reservation reservation) {
//        String recipientEmail = reservation.getGuests().get(0).getEmail();
//        String emailBody = String.format(
//                "Dear %s,\n\nYour reservation has been confirmed.\n\nThank you! \n\n Reservation Details: %s",
//                reservation.getGuests().get(0).getFirstName(),
//                reservation
//        );
//        sendTheEmail(recipientEmail,"Reservation Confirmed", emailBody);
//    }

    public void sendReservationConfirmationEmail(Reservation reservation) {
        String recipientEmail = reservation.getGuests().get(0).getEmail();  // Assuming you want to send the email to the first guest, you can modify as needed.

        // Start the email body with HTML structure and initial content
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("<html>")
                .append("<body>")
                .append("<h2 style='color:#4CAF50;'>Dear %s,</h2>")
                .append("<p>Your reservation has been confirmed!</p>")
                .append("<p>Thank you for choosing us. Here are your reservation details:</p>")
                .append("<h3>Reservation Details:</h3>")
                .append("<table style='border-collapse: collapse; width: 100%;'>")
                .append("<tr>")
                .append("<th style='border: 1px solid #ddd; padding: 8px;'>Field</th>")
                .append("<th style='border: 1px solid #ddd; padding: 8px;'>Details</th>")
                .append("</tr>")
                .append("<tr>")
                .append("<td style='border: 1px solid #ddd; padding: 8px;'>Reservation ID</td>")
                .append("<td style='border: 1px solid #ddd; padding: 8px;'>%d</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td style='border: 1px solid #ddd; padding: 8px;'>Status</td>")
                .append("<td style='border: 1px solid #ddd; padding: 8px;'>%s</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td style='border: 1px solid #ddd; padding: 8px;'>Location</td>")
                .append("<td style='border: 1px solid #ddd; padding: 8px;'>%s</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td style='border: 1px solid #ddd; padding: 8px;'>Patient</td>")
                .append("<td style='border: 1px solid #ddd; padding: 8px;'>%s %s</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td style='border: 1px solid #ddd; padding: 8px;'>Patient Facility</td>")
                .append("<td style='border: 1px solid #ddd; padding: 8px;'>%s</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td style='border: 1px solid #ddd; padding: 8px;'>Condition</td>")
                .append("<td style='border: 1px solid #ddd; padding: 8px;'>%s</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td style='border: 1px solid #ddd; padding: 8px;'>Room Number</td>")
                .append("<td style='border: 1px solid #ddd; padding: 8px;'>%s</td>")
                .append("</tr>");

        // Loop through guests and add their details to the email body
        for (Guest guest : reservation.getGuests()) {
            emailBody.append("<tr>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px;'>Guest Name</td>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px;'>")
                    .append(guest.getFirstName()).append(" ").append(guest.getLastName())
                    .append("</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px;'>Guest Contact</td>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px;'>")
                    .append(guest.getCell())
                    .append("</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px;'>Guest Email</td>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px;'>")
                    .append(guest.getEmail())
                    .append("</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px;'>Check-in Date</td>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px;'>")
                    .append(guest.getCheckInDate())
                    .append("</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px;'>Check-out Date</td>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px;'>")
                    .append(guest.getCheckOutDate())
                    .append("</td>")
                    .append("</tr>");
        }

        // Closing the table and the rest of the email body
        emailBody.append("</table>")
                .append("<p>If you have any questions, feel free to contact us.</p>")
                .append("<p>We look forward to welcoming you!</p>")
                .append("</body>")
                .append("</html>");

        // Send the email with HTML content
        sendEmailHtml(recipientEmail, "Reservation Confirmed", emailBody.toString());
    }

}
