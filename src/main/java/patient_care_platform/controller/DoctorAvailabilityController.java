package patient_care_platform.controller;

import patient_care_platform.model.Doctor;
import patient_care_platform.model.DoctorAvailability;
import patient_care_platform.repository.DoctorAvailabilityRepository;
import patient_care_platform.repository.DoctorRepository;
import patient_care_platform.model.DoctorAvailabilityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/availability")
public class DoctorAvailabilityController {

    @Autowired
    private DoctorAvailabilityRepository availabilityRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @PostMapping
    public ResponseEntity<?> addAvailability(@RequestBody DoctorAvailabilityRequest request) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(request.getDoctorId());

        if (doctorOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Doctor not found");
        }

        DoctorAvailability availability = new DoctorAvailability();
        availability.setDoctor(doctorOptional.get());
        availability.setDayOfWeek(request.getDayOfWeek());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
        availability.setSlotDurationMinutes(request.getSlotDurationMinutes());

        DoctorAvailability saved = availabilityRepository.save(availability);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getAvailabilityForDoctor(@PathVariable Long doctorId) {
        List<DoctorAvailability> availabilityList = availabilityRepository.findByDoctorId(doctorId);
        return ResponseEntity.ok(availabilityList);
    }
}
