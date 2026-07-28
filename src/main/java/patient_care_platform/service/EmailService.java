package patient_care_platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendReminderEmail(String toEmail, String patientName, String doctorName, String date, String time) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Appointment Reminder");
        message.setText(
                "Hi " + patientName + ",\n\n" +
                        "This is a reminder that you have an appointment with Dr. " + doctorName +
                        " on " + date + " at " + time + ".\n\n" +
                        "Please arrive 10 minutes early.\n\n" +
                        "Thank you,\nAI-Assisted Patient Care Platform"
        );
        mailSender.send(message);
    }
}
