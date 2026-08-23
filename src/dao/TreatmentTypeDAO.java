package dao;

import db.DBConnection;
import model.TreatmentType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TreatmentTypeDAO {

    public List<TreatmentType> findAll() {
        List<TreatmentType> list = new ArrayList<>();
        String sql = "SELECT * FROM treatment_type ORDER BY name";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new TreatmentType(rs.getInt("id"), rs.getString("name"), rs.getDouble("cost")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public TreatmentType findById(int id) {
        String sql = "SELECT * FROM treatment_type WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new TreatmentType(rs.getInt("id"), rs.getString("name"), rs.getDouble("cost"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(TreatmentType type) {
        String sql = "INSERT INTO treatment_type (name, cost) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, type.getName());
            ps.setDouble(2, type.getCost());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM treatment_type WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
