package view;

import controller.DoctorController;
import model.Appointment;
import model.DoctorProfile;
import model.Schedule;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorDashboardView extends JFrame {

    private final DoctorController doctorController = new DoctorController();
    private final User currentUser;
    private final DoctorProfile myProfile;

    private final DefaultTableModel apptModel = new DefaultTableModel(
            new Object[]{"Appt #", "Patient", "Treatment", "Date", "Time", "Status", "Notes"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable apptTable = new JTable(apptModel);
    private List<Appointment> currentAppointments;

    private final DefaultTableModel scheduleModel = new DefaultTableModel(
            new Object[]{"ID", "Date", "Start", "End", "Available"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable scheduleTable = new JTable(scheduleModel);

    public DoctorDashboardView(User currentUser) {
        this.currentUser = currentUser;
        this.myProfile = doctorController.myProfile(currentUser.getUsername());
        setTitle("Doctor Dashboard - " + currentUser.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 520);
        setLocationRelativeTo(null);

        if (myProfile == null) {
            JOptionPane.showMessageDialog(this, "No doctor profile found for this login. Contact admin.");
        }

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("My Appointments", buildAppointmentsTab());
        tabs.addTab("My Schedule", buildScheduleTab());
        add(tabs);

        refreshAppointments();
        refreshSchedule();
    }

    private JPanel buildAppointmentsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(apptTable), BorderLayout.CENTER);

        JButton approveButton = new JButton("Approve (Generate Token)");
        approveButton.addActionListener(e -> {
            Appointment selected = selectedAppointment();
            if (selected == null) return;
            doctorController.approve(selected.getId());
            refreshAppointments();
            JOptionPane.showMessageDialog(this, "Approved. Reception can now print the token/bill.");
        });

        JButton rejectButton = new JButton("Reject");
        rejectButton.addActionListener(e -> {
            Appointment selected = selectedAppointment();
            if (selected == null) return;
            doctorController.reject(selected.getId());
            refreshAppointments();
        });

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshAppointments());

        JPanel buttons = new JPanel();
        buttons.add(approveButton);
        buttons.add(rejectButton);
        buttons.add(refreshButton);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildScheduleTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(scheduleTable), BorderLayout.CENTER);

        JButton addButton = new JButton("Add Available Slot");
        addButton.addActionListener(e -> {
            if (myProfile == null) return;
            ScheduleDialog dialog = new ScheduleDialog(this, doctorController, myProfile.getId());
            dialog.setVisible(true);
            if (dialog.wasAdded()) refreshSchedule();
        });

        JButton removeButton = new JButton("Remove Selected Slot");
        removeButton.addActionListener(e -> {
            int row = scheduleTable.getSelectedRow();
            if (row == -1) return;
            int id = (int) scheduleModel.getValueAt(row, 0);
            doctorController.removeSlot(id);
            refreshSchedule();
        });

        JPanel buttons = new JPanel();
        buttons.add(addButton);
        buttons.add(removeButton);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private Appointment selectedAppointment() {
        int row = apptTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an appointment first");
            return null;
        }
        return currentAppointments.get(row);
    }

    private void refreshAppointments() {
        if (myProfile == null) return;
        currentAppointments = doctorController.myAppointments(myProfile.getId());
        apptModel.setRowCount(0);
        for (Appointment a : currentAppointments) {
            apptModel.addRow(new Object[]{
                    a.getAppointmentNumber(), a.getPatientName(), a.getTreatmentTypeName(),
                    a.getAppointmentDate(), a.getAppointmentTime(), a.getStatus(), a.getNotes()
            });
        }
    }

    private void refreshSchedule() {
        if (myProfile == null) return;
        List<Schedule> slots = doctorController.mySchedule(myProfile.getId());
        scheduleModel.setRowCount(0);
        for (Schedule s : slots) {
            scheduleModel.addRow(new Object[]{
                    s.getId(), s.getDate(), s.getStartTime(), s.getEndTime(), s.isAvailable() ? "Yes" : "No"
            });
        }
    }
}
