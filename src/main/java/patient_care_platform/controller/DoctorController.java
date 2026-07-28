package patient_care_platform.controller;

import patient_care_platform.model.Doctor;
import patient_care_platform.model.DoctorProfileRequest;
import patient_care_platform.model.User;
import patient_care_platform.repository.DoctorRepository;
import patient_care_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/profile")
    public ResponseEntity<?> createDoctorProfile(@RequestBody DoctorProfileRequest request) {
        Optional<User> userOptional = userRepository.findById(request.getUserId());

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Doctor doctor = new Doctor();
        doctor.setUser(userOptional.get());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setYearsExperience(request.getYearsExperience());

        Doctor savedDoctor = doctorRepository.save(doctor);
        return ResponseEntity.ok(savedDoctor);
    }
}