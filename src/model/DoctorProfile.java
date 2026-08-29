package model;

public class DoctorProfile {
    private int id;
    private int userId;
    private String username;   // convenience: login username, joined in from users table
    private String fullName;
    private String specialization;
    private String contactNumber;
    private double consultationFee;
    private boolean available;

    public DoctorProfile() {}

    public DoctorProfile(int id, int userId, String username, String fullName, String specialization,
                          String contactNumber, double consultationFee, boolean available) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.specialization = specialization;
        this.contactNumber = contactNumber;
        this.consultationFee = consultationFee;
        this.available = available;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        
        return fullName + " (" + specialization + ")";
    }
}
