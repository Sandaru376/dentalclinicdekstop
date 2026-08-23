package dao;

import db.DBConnection;
import model.DoctorProfile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    private static final String BASE_SELECT =
            "SELECT dp.*, u.username FROM doctor_profile dp JOIN users u ON dp.user_id = u.id ";

    public DoctorProfile save(DoctorProfile profile) {
        String sql = "INSERT INTO doctor_profile (user_id, full_name, specialization, contact_number, consultation_fee, available) " +
                "VALUES (?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFullName());
            ps.setString(3, profile.getSpecialization());
            ps.setString(4, profile.getContactNumber());
            ps.setDouble(5, profile.getConsultationFee());
            ps.setBoolean(6, profile.isAvailable());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) profile.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profile;
    }

    public List<DoctorProfile> findAll() {
        List<DoctorProfile> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(BASE_SELECT + "ORDER BY dp.full_name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<DoctorProfile> findAvailable() {
        List<DoctorProfile> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(BASE_SELECT + "WHERE dp.available = TRUE ORDER BY dp.full_name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public DoctorProfile findById(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(BASE_SELECT + "WHERE dp.id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DoctorProfile findByUsername(String username) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(BASE_SELECT + "WHERE u.username = ?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setAvailability(int id, boolean available) {
        String sql = "UPDATE doctor_profile SET available = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, available);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateOwnProfile(int id, String specialization, String contactNumber, double consultationFee) {
        String sql = "UPDATE doctor_profile SET specialization=?, contact_number=?, consultation_fee=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, specialization);
            ps.setString(2, contactNumber);
            ps.setDouble(3, consultationFee);
            ps.setInt(4, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DoctorProfile map(ResultSet rs) throws SQLException {
        return new DoctorProfile(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("full_name"),
                rs.getString("specialization"),
                rs.getString("contact_number"),
                rs.getDouble("consultation_fee"),
                rs.getBoolean("available")
        );
    }
}
