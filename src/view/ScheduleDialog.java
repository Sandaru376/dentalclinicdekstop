package view;

import controller.DoctorController;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;

public class ScheduleDialog extends JDialog {

    private final JTextField dateField = new JTextField("yyyy-MM-dd", 10);
    private final JTextField startField = new JTextField("HH:mm", 6);
    private final JTextField endField = new JTextField("HH:mm", 6);
    private final DoctorController doctorController;
    private final int doctorId;
    private boolean added = false;

    public ScheduleDialog(JFrame parent, DoctorController doctorController, int doctorId) {
        super(parent, "Add Available Time Slot", true);
        this.doctorController = doctorController;
        this.doctorId = doctorId;
        setSize(320, 220);
        setLocationRelativeTo(parent);

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        form.add(new JLabel("Date (yyyy-MM-dd):")); form.add(dateField);
        form.add(new JLabel("Start time (HH:mm):")); form.add(startField);
        form.add(new JLabel("End time (HH:mm):")); form.add(endField);

        JButton saveButton = new JButton("Add Slot");
        saveButton.addActionListener(e -> onSave());
        JPanel bottom = new JPanel();
        bottom.add(saveButton);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void onSave() {
        try {
            Date date = Date.valueOf(dateField.getText().trim());
            Time start = Time.valueOf(startField.getText().trim() + ":00");
            Time end = Time.valueOf(endField.getText().trim() + ":00");
            doctorController.addSlot(doctorId, date, start, end);
            added = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Check the date/time format:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean wasAdded() { return added; }
}
