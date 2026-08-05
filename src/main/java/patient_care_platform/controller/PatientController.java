package patient_care_platform.controller;

import patient_care_platform.model.Patient;
import patient_care_platform.model.PatientProfileRequest;
import patient_care_platform.model.User;
import patient_care_platform.repository.PatientRepository;
import patient_care_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/profile")
    public ResponseEntity<?> createPatientProfile(@RequestBody PatientProfileRequest request) {
        Optional<User> userOptional = userRepository.findById(request.getUserId());

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Patient patient = new Patient();
        patient.setUser(userOptional.get());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setMedicalHistory(request.getMedicalHistory());
        patient.setEmergencyContact(request.getEmergencyContact());

        Patient savedPatient = patientRepository.save(patient);
        return ResponseEntity.ok(savedPatient);
    }

    @GetMapping
    public ResponseEntity<?> getAllPatients() {
        return ResponseEntity.ok(patientRepository.findAll());
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<?> getPatientByUserId(@PathVariable Long userId) {
        Optional<Patient> patientOptional = patientRepository.findByUserId(userId);
        if (patientOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Patient profile not found");
        }
        return ResponseEntity.ok(patientOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id) {
        if (!patientRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Patient not found");
        }
        patientRepository.deleteById(id);
        return ResponseEntity.ok("Patient deleted successfully");
    }
}