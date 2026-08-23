package controller;

import dao.DoctorDAO;
import dao.TreatmentTypeDAO;
import dao.UserDAO;
import model.DoctorProfile;
import model.TreatmentType;
import model.User;
import util.PasswordUtil;

import java.util.List;

/** Everything the ADMIN role can do: create doctor/reception accounts, manage prices. */
public class AdminController {

    private final UserDAO userDAO = new UserDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final TreatmentTypeDAO treatmentTypeDAO = new TreatmentTypeDAO();

    public DoctorProfile createDoctor(String username, String rawPassword, String fullName,
                                       String specialization, String contactNumber,
                                       double consultationFee) {
        if (userDAO.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username already taken");
        }
        User user = new User(0, username, PasswordUtil.hash(rawPassword), "DOCTOR", fullName, contactNumber);
        int userId = userDAO.save(user);
        if (userId == -1) throw new IllegalStateException("Could not create login account");

        DoctorProfile profile = new DoctorProfile();
        profile.setUserId(userId);
        profile.setFullName(fullName);
        profile.setSpecialization(specialization);
        profile.setContactNumber(contactNumber);
        profile.setConsultationFee(consultationFee);
        profile.setAvailable(true);
        return doctorDAO.save(profile);
    }

    public User createReception(String username, String rawPassword, String fullName, String contactNumber) {
        if (userDAO.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username already taken");
        }
        User user = new User(0, username, PasswordUtil.hash(rawPassword), "RECEPTION", fullName, contactNumber);
        int id = userDAO.save(user);
        user.setId(id);
        return user;
    }

    public List<DoctorProfile> listDoctors() { return doctorDAO.findAll(); }

    public List<User> listReception() { return userDAO.findByRole("RECEPTION"); }

    public void setDoctorAvailability(int doctorId, boolean available) {
        doctorDAO.setAvailability(doctorId, available);
    }

    public List<TreatmentType> listTreatmentTypes() { return treatmentTypeDAO.findAll(); }

    public void addTreatmentType(String name, double cost) {
        treatmentTypeDAO.save(new TreatmentType(0, name, cost));
    }

    public void deleteTreatmentType(int id) { treatmentTypeDAO.delete(id); }
}
