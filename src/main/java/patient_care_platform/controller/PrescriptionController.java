package patient_care_platform.controller;

import patient_care_platform.model.Appointment;
import patient_care_platform.model.Prescription;
import patient_care_platform.model.PrescriptionRequest;
import patient_care_platform.repository.AppointmentRepository;
import patient_care_platform.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @PostMapping
    public ResponseEntity<?> createPrescription(@RequestBody PrescriptionRequest request) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(request.getAppointmentId());

        if (appointmentOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Appointment not found");
        }

        Prescription prescription = new Prescription();
        prescription.setAppointment(appointmentOptional.get());
        prescription.setMedicationName(request.getMedicationName());
        prescription.setDosage(request.getDosage());
        prescription.setFrequency(request.getFrequency());
        prescription.setDuration(request.getDuration());
        prescription.setNotes(request.getNotes());

        Prescription saved = prescriptionRepository.save(prescription);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getPrescriptionsForAppointment(@PathVariable Long appointmentId) {
        List<Prescription> prescriptions = prescriptionRepository.findByAppointmentId(appointmentId);
        return ResponseEntity.ok(prescriptions);
    }
}
