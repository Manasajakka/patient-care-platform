package patient_care_platform.controller;

import patient_care_platform.model.*;
import patient_care_platform.repository.AppointmentRepository;
import patient_care_platform.repository.DoctorRepository;
import patient_care_platform.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patient_care_platform.service.EmailService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequest request) {
        Optional<Patient> patientOptional = patientRepository.findById(request.getPatientId());
        Optional<Doctor> doctorOptional = doctorRepository.findById(request.getDoctorId());

        if (patientOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Patient not found");
        }
        if (doctorOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Doctor not found");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patientOptional.get());
        appointment.setDoctor(doctorOptional.get());
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());

        try {
            Appointment saved = appointmentRepository.save(appointment);
            return ResponseEntity.ok(saved);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(409).body("This time slot is already booked");
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getAppointmentsForPatient(@PathVariable Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return ResponseEntity.ok(appointments);
    }
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getAppointmentsForDoctor(@PathVariable Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return ResponseEntity.ok(appointments);
    }
    @PostMapping("/{appointmentId}/send-reminder")
    public ResponseEntity<?> sendReminder(@PathVariable Long appointmentId) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);

        if (appointmentOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Appointment not found");
        }

        Appointment appointment = appointmentOptional.get();
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

        return ResponseEntity.ok("Reminder email sent to " + patientEmail);
    }
}
