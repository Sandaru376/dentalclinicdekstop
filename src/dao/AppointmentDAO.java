package dao;

import db.DBConnection;
import model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    private static final String BASE_SELECT =
        "SELECT a.*, dp.full_name AS doctor_name, dp.consultation_fee, " +
        "tt.name AS treatment_name, tt.cost AS treatment_cost " +
        "FROM appointment a " +
        "JOIN doctor_profile dp ON a.doctor_id = dp.id " +
        "JOIN treatment_type tt ON a.treatment_type_id = tt.id ";

    public long count() {
        String sql = "SELECT COUNT(*) FROM appointment";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String generateAppointmentNumber() {
        return String.format("APT-%06d", count() + 1);
    }

    public Appointment save(Appointment appt) {
        if (appt.getAppointmentNumber() == null) {
            appt.setAppointmentNumber(generateAppointmentNumber());
        }
        if (appt.getStatus() == null) {
            appt.setStatus("PENDING");
        }
        String sql = "INSERT INTO appointment " +
                "(appointment_number, patient_name, patient_contact, doctor_id, treatment_type_id, " +
                " appointment_date, appointment_time, status, notes, created_by, created_at) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, appt.getAppointmentNumber());
            ps.setString(2, appt.getPatientName());
            ps.setString(3, appt.getPatientContact());
            ps.setInt(4, appt.getDoctorId());
            ps.setInt(5, appt.getTreatmentTypeId());
            ps.setDate(6, appt.getAppointmentDate());
            ps.setTime(7, appt.getAppointmentTime());
            ps.setString(8, appt.getStatus());
            ps.setString(9, appt.getNotes());
            ps.setString(10, appt.getCreatedBy());
            ps.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) appt.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appt;
    }

    public List<Appointment> findAll() {
        return query(BASE_SELECT + "ORDER BY a.created_at DESC");
    }

    public List<Appointment> findByStatus(String status) {
        List<Appointment> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE a.status = ? ORDER BY a.appointment_date, a.appointment_time";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Appointment> findByDoctor(int doctorId) {
        List<Appointment> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE a.doctor_id = ? ORDER BY a.appointment_date, a.appointment_time";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Appointment findById(int id) {
        String sql = BASE_SELECT + "WHERE a.id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Appointment findByAppointmentNumber(String number) {
        String sql = BASE_SELECT + "WHERE a.appointment_number = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, number);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Doctor approves: generate a token number and lock in the bill total. */
    public Appointment accept(int id) {
        Appointment appt = findById(id);
        if (appt == null) return null;
        String token = "TKN-" + appt.getAppointmentNumber().substring(4);
        double total = appt.getConsultationFee() + appt.getTreatmentCost();
        String sql = "UPDATE appointment SET status='ACCEPTED', token_number=?, total_cost=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.setDouble(2, total);
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return findById(id);
    }

    public Appointment reject(int id) {
        String sql = "UPDATE appointment SET status='REJECTED' WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return findById(id);
    }

    private List<Appointment> query(String sql) {
        List<Appointment> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Appointment map(ResultSet rs) throws SQLException {
        Appointment a = new Appointment();
        a.setId(rs.getInt("id"));
        a.setAppointmentNumber(rs.getString("appointment_number"));
        a.setPatientName(rs.getString("patient_name"));
        a.setPatientContact(rs.getString("patient_contact"));
        a.setDoctorId(rs.getInt("doctor_id"));
        a.setDoctorName(rs.getString("doctor_name"));
        a.setConsultationFee(rs.getDouble("consultation_fee"));
        a.setTreatmentTypeId(rs.getInt("treatment_type_id"));
        a.setTreatmentTypeName(rs.getString("treatment_name"));
        a.setTreatmentCost(rs.getDouble("treatment_cost"));
        a.setAppointmentDate(rs.getDate("appointment_date"));
        a.setAppointmentTime(rs.getTime("appointment_time"));
        a.setStatus(rs.getString("status"));
        a.setTokenNumber(rs.getString("token_number"));
        a.setNotes(rs.getString("notes"));
        a.setTotalCost(rs.getDouble("total_cost"));
        a.setCreatedBy(rs.getString("created_by"));
        a.setCreatedAt(rs.getTimestamp("created_at"));
        return a;
    }
}
