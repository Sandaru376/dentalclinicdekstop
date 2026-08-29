package controller;

import dao.AppointmentDAO;
import model.Appointment;

public class AppointmentSearchController {

    private final AppointmentDAO appointmentDAO =
            new AppointmentDAO();

    public Appointment search(String appointmentNumber) {
        if (appointmentNumber == null
                || appointmentNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Please enter an appointment number."
            );
        }

        return appointmentDAO.findByAppointmentNumber(
                appointmentNumber.trim().toUpperCase()
        );
    }
}