package controller;

import dao.AppointmentDAO;
import dao.DoctorDAO;
import dao.TreatmentTypeDAO;
import model.Appointment;
import model.DoctorProfile;
import model.TreatmentType;

import java.sql.Date;
import java.sql.Time;
import java.util.List;


public class ReceptionController {

    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final TreatmentTypeDAO treatmentTypeDAO = new TreatmentTypeDAO();

    public List<DoctorProfile> availableDoctors() { return doctorDAO.findAvailable(); }

    public List<TreatmentType> treatmentTypes() { return treatmentTypeDAO.findAll(); }

    public Appointment bookAppointment(String patientName, String patientContact, int doctorId,
                                        int treatmentTypeId, Date date, Time time,
                                        String notes, String receptionUsername) {
        if (patientName == null || patientName.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name is required");
        }
        DoctorProfile doctor = doctorDAO.findById(doctorId);
        if (doctor == null || !doctor.isAvailable()) {
            throw new IllegalArgumentException("Selected doctor is not available");
        }
        Appointment appt = new Appointment();
        appt.setPatientName(patientName.trim());
        appt.setPatientContact(patientContact);
        appt.setDoctorId(doctorId);
        appt.setTreatmentTypeId(treatmentTypeId);
        appt.setAppointmentDate(date);
        appt.setAppointmentTime(time);
        appt.setNotes(notes);
        appt.setCreatedBy(receptionUsername);
        return appointmentDAO.save(appt);
    }

    public List<Appointment> allAppointments() { return appointmentDAO.findAll(); }

    public List<Appointment> appointmentsByStatus(String status) { return appointmentDAO.findByStatus(status); }

    public Appointment findByAppointmentNumber(String number) { return appointmentDAO.findByAppointmentNumber(number); }
}
