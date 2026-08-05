package patient_care_platform.service;

import patient_care_platform.model.Appointment;
import patient_care_platform.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ReminderScheduler {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedRate = 1000)
    public void sendUpcomingReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<Appointment> appointments =
                appointmentRepository.findByAppointmentDateAndReminderSentFalse(tomorrow);

        for (Appointment appointment : appointments) {
            try {
                String patientEmail = appointment.getPatient().getUser().getEmail();
                String patientName = appointment.getPatient().getUser().getFirstName();
                String doctorName = appointment.getDoctor().getUser().getFirstName();

                emailService.sendReminderEmail(
                        patientEmail,
                        patientName,
                        doctorName,
                        appointment.getAppointmentDate().toString(),
                        appointment.getAppointmentTime().toString()
                );

                appointment.setReminderSent(true);
                appointmentRepository.save(appointment);

                System.out.println("Reminder sent for appointment ID: " + appointment.getId());
            } catch (Exception e) {
                System.out.println("Failed to send reminder for appointment ID: " + appointment.getId());
            }
        }
    }
}