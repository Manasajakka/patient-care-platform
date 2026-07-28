package patient_care_platform.controller;

import patient_care_platform.model.Appointment;
import patient_care_platform.model.Doctor;
import patient_care_platform.model.DoctorAvailability;
import patient_care_platform.model.DoctorAvailabilityRequest;
import patient_care_platform.repository.AppointmentRepository;
import patient_care_platform.repository.DoctorAvailabilityRepository;
import patient_care_platform.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/availability")
public class DoctorAvailabilityController {

    @Autowired
    private DoctorAvailabilityRepository availabilityRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

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

    @GetMapping("/doctor/{doctorId}/open-slots")
    public ResponseEntity<?> getOpenSlots(@PathVariable Long doctorId, @RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        List<DoctorAvailability> availabilityList = availabilityRepository.findByDoctorId(doctorId);

        List<DoctorAvailability> matchingAvailability = availabilityList.stream()
                .filter(a -> a.getDayOfWeek() == targetDate.getDayOfWeek())
                .collect(Collectors.toList());

        if (matchingAvailability.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<LocalTime>());
        }

        List<Appointment> existingAppointments = appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, targetDate);
        Set<LocalTime> bookedTimes = existingAppointments.stream()
                .map(Appointment::getAppointmentTime)
                .collect(Collectors.toSet());

        List<LocalTime> openSlots = new ArrayList<>();

        for (DoctorAvailability availability : matchingAvailability) {
            LocalTime slotTime = availability.getStartTime();
            while (slotTime.isBefore(availability.getEndTime())) {
                if (!bookedTimes.contains(slotTime)) {
                    openSlots.add(slotTime);
                }
                slotTime = slotTime.plusMinutes(availability.getSlotDurationMinutes());
            }
        }

        return ResponseEntity.ok(openSlots);
    }
}