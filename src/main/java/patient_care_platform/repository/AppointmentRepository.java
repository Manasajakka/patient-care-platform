package patient_care_platform.repository;

import patient_care_platform.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDate appointmentDate);
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorId(Long doctorId);
    long countByStatus(Appointment.AppointmentStatus status);
    long countByDoctorId(Long doctorId);
    List<Appointment> findByAppointmentDateBetween(LocalDate startDate, LocalDate endDate);
}