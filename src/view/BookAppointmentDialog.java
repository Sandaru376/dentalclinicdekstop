package view;

import controller.ReceptionController;
import model.DoctorProfile;
import model.TreatmentType;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class BookAppointmentDialog extends JDialog {

    private final JTextField patientNameField = new JTextField(15);
    private final JTextField patientContactField = new JTextField(15);
    private final JComboBox<DoctorProfile> doctorCombo = new JComboBox<>();
    private final JComboBox<TreatmentType> treatmentCombo = new JComboBox<>();
    private final JTextField dateField = new JTextField("yyyy-MM-dd", 10);
    private final JTextField timeField = new JTextField("HH:mm", 6);
    private final JTextArea notesArea = new JTextArea(3, 15);

    private final ReceptionController receptionController;
    private final User currentUser;
    private boolean booked = false;

    public BookAppointmentDialog(JFrame parent, ReceptionController receptionController, User currentUser) {
        super(parent, "Book Appointment", true);
        this.receptionController = receptionController;
        this.currentUser = currentUser;
        setSize(400, 420);
        setLocationRelativeTo(parent);

        for (DoctorProfile d : receptionController.availableDoctors()) doctorCombo.addItem(d);
        for (TreatmentType t : receptionController.treatmentTypes()) treatmentCombo.addItem(t);

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        form.add(new JLabel("Patient name:")); form.add(patientNameField);
        form.add(new JLabel("Patient contact:")); form.add(patientContactField);
        form.add(new JLabel("Doctor:")); form.add(doctorCombo);
        form.add(new JLabel("Treatment type:")); form.add(treatmentCombo);
        form.add(new JLabel("Date (yyyy-MM-dd):")); form.add(dateField);
        form.add(new JLabel("Time (HH:mm):")); form.add(timeField);
        form.add(new JLabel("Notes:")); form.add(new JScrollPane(notesArea));

        JButton saveButton = new JButton("Book Appointment");
        saveButton.addActionListener(e -> onSave());
        JPanel bottom = new JPanel();
        bottom.add(saveButton);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void onSave() {
        try {
            DoctorProfile doctor = (DoctorProfile) doctorCombo.getSelectedItem();
            TreatmentType treatment = (TreatmentType) treatmentCombo.getSelectedItem();
            if (doctor == null || treatment == null) {
                JOptionPane.showMessageDialog(this, "Add a doctor and treatment type first (ask an admin)");
                return;
            }
            Date date = Date.valueOf(dateField.getText().trim());
            Time time = parseTime(timeField.getText().trim());

            var appt = receptionController.bookAppointment(
                    patientNameField.getText(),
                    patientContactField.getText(),
                    doctor.getId(),
                    treatment.getId(),
                    date, time,
                    notesArea.getText(),
                    currentUser.getUsername()
            );
            JOptionPane.showMessageDialog(this, "Booked! Appointment number: " + appt.getAppointmentNumber());
            booked = true;
            dispose();
        } catch (IllegalArgumentException dateEx) {
            JOptionPane.showMessageDialog(this, "Check the date/time format or required fields:\n" + dateEx.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Time parseTime(String text) {
        // Accept "HH:mm" and turn it into a java.sql.Time
        String[] parts = text.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        return Time.valueOf(String.format("%02d:%02d:00", hour, minute));
    }

    public boolean wasBooked() { return booked; }
}
