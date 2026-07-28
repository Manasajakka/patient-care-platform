package patient_care_platform.controller;

import patient_care_platform.model.Appointment;
import patient_care_platform.model.DashboardStats;
import patient_care_platform.repository.AppointmentRepository;
import patient_care_platform.repository.DoctorRepository;
import patient_care_platform.repository.PatientRepository;
import patient_care_platform.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats() {
        DashboardStats stats = new DashboardStats();

        stats.setTotalDoctors(doctorRepository.count());
        stats.setTotalPatients(patientRepository.count());
        stats.setTotalAppointments(appointmentRepository.count());
        stats.setTotalPrescriptions(prescriptionRepository.count());

        Map<String, Long> statusCounts = new HashMap<>();
        for (Appointment.AppointmentStatus status : Appointment.AppointmentStatus.values()) {
            long count = appointmentRepository.countByStatus(status);
            statusCounts.put(status.name(), count);
        }
        stats.setAppointmentsByStatus(statusCounts);

        return ResponseEntity.ok(stats);
    }
}
