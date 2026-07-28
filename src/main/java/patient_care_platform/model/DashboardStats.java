package patient_care_platform.model;

import java.util.Map;

public class DashboardStats {
    private long totalDoctors;
    private long totalPatients;
    private long totalAppointments;
    private long totalPrescriptions;
    private Map<String, Long> appointmentsByStatus;

    public long getTotalDoctors() {
        return totalDoctors;
    }

    public void setTotalDoctors(long totalDoctors) {
        this.totalDoctors = totalDoctors;
    }

    public long getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(long totalPatients) {
        this.totalPatients = totalPatients;
    }

    public long getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(long totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public long getTotalPrescriptions() {
        return totalPrescriptions;
    }

    public void setTotalPrescriptions(long totalPrescriptions) {
        this.totalPrescriptions = totalPrescriptions;
    }

    public Map<String, Long> getAppointmentsByStatus() {
        return appointmentsByStatus;
    }

    public void setAppointmentsByStatus(Map<String, Long> appointmentsByStatus) {
        this.appointmentsByStatus = appointmentsByStatus;
    }
}
