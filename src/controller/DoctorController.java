package controller;

import dao.AppointmentDAO;
import dao.DoctorDAO;
import dao.ScheduleDAO;
import model.Appointment;
import model.DoctorProfile;
import model.Schedule;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

/** Everything the DOCTOR role can do: see appointments reception booked for them,
 *  approve (generates the token) or reject, and manage their own available time slots. */
public class DoctorController {

    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final ScheduleDAO scheduleDAO = new ScheduleDAO();

    public DoctorProfile myProfile(String username) { return doctorDAO.findByUsername(username); }

    public List<Appointment> myAppointments(int doctorId) { return appointmentDAO.findByDoctor(doctorId); }

    /** Approve: generates the token number reception will hand to the patient, and locks the bill total. */
    public Appointment approve(int appointmentId) { return appointmentDAO.accept(appointmentId); }

    public Appointment reject(int appointmentId) { return appointmentDAO.reject(appointmentId); }

    public void updateOwnProfile(int doctorId, String specialization, String contactNumber, double consultationFee) {
        doctorDAO.updateOwnProfile(doctorId, specialization, contactNumber, consultationFee);
    }

    public List<Schedule> mySchedule(int doctorId) { return scheduleDAO.findByDoctor(doctorId); }

    public Schedule addSlot(int doctorId, Date date, Time start, Time end) {
        if (!end.after(start)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        Schedule schedule = new Schedule();
        schedule.setDoctorId(doctorId);
        schedule.setDate(date);
        schedule.setStartTime(start);
        schedule.setEndTime(end);
        return scheduleDAO.save(schedule);
    }

    public void removeSlot(int scheduleId) { scheduleDAO.delete(scheduleId); }
}
