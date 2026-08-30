/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.DBConnection;
import model.Appointment;
import model.DailyReportSummary;
import model.DailyRevenueRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    private static final String APPOINTMENT_SELECT =
            "SELECT a.*, dp.full_name AS doctor_name, dp.consultation_fee, " +
            "tt.name AS treatment_name, tt.cost AS treatment_cost " +
            "FROM appointment a " +
            "JOIN doctor_profile dp ON a.doctor_id = dp.id " +
            "JOIN treatment_type tt ON a.treatment_type_id = tt.id ";

    public List<Appointment> findAppointmentsByDate(Date date) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = APPOINTMENT_SELECT +
                "WHERE a.appointment_date = ? ORDER BY a.appointment_time";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, date);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) appointments.add(mapAppointment(result));
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Could not load the daily appointment report.", exception);
        }
        return appointments;
    }

    public DailyReportSummary getDailySummary(Date date) {
        String sql = "SELECT COUNT(*) total, " +
                "COALESCE(SUM(CASE WHEN status='ACCEPTED' THEN 1 ELSE 0 END),0) accepted, " +
                "COALESCE(SUM(CASE WHEN status='PENDING' THEN 1 ELSE 0 END),0) pending, " +
                "COALESCE(SUM(CASE WHEN status='REJECTED' THEN 1 ELSE 0 END),0) rejected " +
                "FROM appointment WHERE appointment_date = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, date);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new DailyReportSummary(result.getInt("total"), result.getInt("accepted"),
                            result.getInt("pending"), result.getInt("rejected"));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Could not calculate the daily summary.", exception);
        }
        return new DailyReportSummary(0, 0, 0, 0);
    }

    public List<DailyRevenueRow> getMonthlyRevenue(Date startDate, Date endDate) {
        List<DailyRevenueRow> rows = new ArrayList<>();
        String sql = "SELECT appointment_date, COUNT(*) appointment_count, " +
                "COALESCE(SUM(total_cost),0) revenue FROM appointment " +
                "WHERE status='ACCEPTED' AND appointment_date >= ? AND appointment_date < ? " +
                "GROUP BY appointment_date ORDER BY appointment_date";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, startDate);
            statement.setDate(2, endDate);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    rows.add(new DailyRevenueRow(result.getDate("appointment_date"),
                            result.getInt("appointment_count"), result.getDouble("revenue")));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Could not load the monthly revenue report.", exception);
        }
        return rows;
    }

    private Appointment mapAppointment(ResultSet result) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(result.getInt("id"));
        appointment.setAppointmentNumber(result.getString("appointment_number"));
        appointment.setPatientName(result.getString("patient_name"));
        appointment.setPatientContact(result.getString("patient_contact"));
        appointment.setDoctorId(result.getInt("doctor_id"));
        appointment.setDoctorName(result.getString("doctor_name"));
        appointment.setTreatmentTypeId(result.getInt("treatment_type_id"));
        appointment.setTreatmentTypeName(result.getString("treatment_name"));
        appointment.setAppointmentDate(result.getDate("appointment_date"));
        appointment.setAppointmentTime(result.getTime("appointment_time"));
        appointment.setStatus(result.getString("status"));
        appointment.setTotalCost(result.getDouble("total_cost"));
        return appointment;
    }
}
