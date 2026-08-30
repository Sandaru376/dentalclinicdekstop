package view;

import controller.DoctorController;
import model.Appointment;
import model.DoctorProfile;
import model.Schedule;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * NetBeans GUI Builder form (paired DoctorDashboardView.form) - edit via Design tab.
 */
public class DoctorDashboardView extends JFrame {

    private final DoctorController doctorController = new DoctorController();
    private final User currentUser;
    private final DoctorProfile myProfile;
    private List<Appointment> currentAppointments;
    private List<Schedule> currentSlots;
    private final JLabel todayLabel = new JLabel("Today: 0", SwingConstants.CENTER);
    private final JLabel pendingLabel = new JLabel("Pending: 0", SwingConstants.CENTER);
    private final JLabel acceptedLabel = new JLabel("Accepted: 0", SwingConstants.CENTER);
    private final JLabel slotsLabel = new JLabel("Available slots: 0", SwingConstants.CENTER);

    private final DefaultTableModel apptModel = new DefaultTableModel(
            new Object[]{"Appt #", "Patient", "Treatment", "Date", "Time", "Status", "Notes"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final DefaultTableModel scheduleModel = new DefaultTableModel(
            new Object[]{"ID", "Date", "Start", "End", "Available"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };

    public DoctorDashboardView(User currentUser) {
        this.currentUser = currentUser;
        this.myProfile = doctorController.myProfile(currentUser.getUsername());
        setTitle("Doctor Dashboard - " + currentUser.getFullName());
        initComponents();
        installDashboardHeader();
        UITheme.setWindowIcon(this, "/resources/doctoricon.png");
        UITheme.style(this);
        if (myProfile == null) {
            JOptionPane.showMessageDialog(this, "No doctor profile found for this login. Contact admin.");
        }
        apptTable.setModel(apptModel);
        scheduleTable.setModel(scheduleModel);
        UITheme.installEmptyState(apptScrollPane, apptTable, "No appointments are available.");
        UITheme.installEmptyState(scheduleScrollPane, scheduleTable, "No availability slots have been added.");
        setMinimumSize(new java.awt.Dimension(900, 560));
        setSize(1020, 620);
        setLocationRelativeTo(null);
        refreshAppointments();
        refreshSchedule();
    }

    private void installDashboardHeader() {
        java.awt.Container dashboardContent = getContentPane();
        JPanel header = new JPanel(new java.awt.BorderLayout(12, 0));
        header.setBackground(UITheme.SURFACE_ALT);
        header.putClientProperty("ui.preserveBackground", Boolean.TRUE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        JLabel heading = new JLabel("Doctor Dashboard");
        heading.setFont(heading.getFont().deriveFont(java.awt.Font.BOLD, 21f));
        ImageIcon icon = UITheme.resourceIcon("/resources/doctoricon.png", 34, 34);
        if (icon != null) { heading.setIcon(icon); heading.setIconTextGap(10); }
        JLabel profile = new JLabel(currentUser.getFullName() + (myProfile == null ? "" : "  •  " + myProfile.getSpecialization()));
        profile.setForeground(UITheme.MUTED);
        profile.putClientProperty("ui.preserveForeground", Boolean.TRUE);
        header.add(heading, java.awt.BorderLayout.WEST);
        header.add(profile, java.awt.BorderLayout.EAST);
        JPanel summaries = new JPanel(new java.awt.GridLayout(1, 4, 10, 0));
        summaries.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        summaries.add(summaryCard(todayLabel, UITheme.ACCENT));
        summaries.add(summaryCard(pendingLabel, new java.awt.Color(251, 191, 36)));
        summaries.add(summaryCard(acceptedLabel, new java.awt.Color(34, 197, 94)));
        summaries.add(summaryCard(slotsLabel, new java.awt.Color(56, 189, 248)));
        JPanel top = new JPanel(new java.awt.BorderLayout());
        top.add(header, java.awt.BorderLayout.NORTH);
        top.add(summaries, java.awt.BorderLayout.CENTER);
        JPanel shell = new JPanel(new java.awt.BorderLayout());
        shell.add(top, java.awt.BorderLayout.NORTH);
        shell.add(dashboardContent, java.awt.BorderLayout.CENTER);
        setContentPane(shell);
    }

    private JPanel summaryCard(JLabel label, java.awt.Color accent) {
        label.setFont(label.getFont().deriveFont(java.awt.Font.BOLD, 15f));
        JPanel card = new JPanel(new java.awt.BorderLayout());
        card.setBackground(UITheme.SURFACE_ALT);
        card.putClientProperty("ui.preserveBackground", Boolean.TRUE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 1, 1, 1, accent),
                BorderFactory.createEmptyBorder(9, 7, 9, 7)));
        card.add(label);
        return card;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabs = new javax.swing.JTabbedPane();
        appointmentsPanel = new javax.swing.JPanel();
        apptScrollPane = new javax.swing.JScrollPane();
        apptTable = new javax.swing.JTable();
        approveButton = new javax.swing.JButton();
        rejectButton = new javax.swing.JButton();
        refreshAppointmentsButton = new javax.swing.JButton();
        logoutButton = new javax.swing.JButton();
        schedulePanel = new javax.swing.JPanel();
        scheduleScrollPane = new javax.swing.JScrollPane();
        scheduleTable = new javax.swing.JTable();
        addSlotButton = new javax.swing.JButton();
        removeSlotButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        apptScrollPane.setViewportView(apptTable);

        approveButton.setText("Approve (Generate Token)");
        approveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                approveButtonActionPerformed(evt);
            }
        });

        rejectButton.setText("Reject");
        rejectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rejectButtonActionPerformed(evt);
            }
        });

        refreshAppointmentsButton.setText("Refresh");
        refreshAppointmentsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshAppointmentsButtonActionPerformed(evt);
            }
        });

        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout appointmentsPanelLayout = new javax.swing.GroupLayout(appointmentsPanel);
        appointmentsPanel.setLayout(appointmentsPanelLayout);
        appointmentsPanelLayout.setHorizontalGroup(
            appointmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(appointmentsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(appointmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(appointmentsPanelLayout.createSequentialGroup()
                        .addComponent(apptScrollPane)
                        .addContainerGap())
                    .addGroup(appointmentsPanelLayout.createSequentialGroup()
                        .addComponent(approveButton)
                        .addGap(18, 18, 18)
                        .addComponent(rejectButton)
                        .addGap(18, 18, 18)
                        .addComponent(refreshAppointmentsButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 253, Short.MAX_VALUE)
                        .addComponent(logoutButton)
                        .addGap(201, 201, 201))))
        );
        appointmentsPanelLayout.setVerticalGroup(
            appointmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(appointmentsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(apptScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(appointmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(approveButton)
                    .addComponent(rejectButton)
                    .addComponent(refreshAppointmentsButton)
                    .addComponent(logoutButton))
                .addContainerGap())
        );

        tabs.addTab("Appointments", appointmentsPanel);

        scheduleScrollPane.setViewportView(scheduleTable);

        addSlotButton.setText("Add Available Slot");
        addSlotButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSlotButtonActionPerformed(evt);
            }
        });

        removeSlotButton.setText("Remove Selected Slot");
        removeSlotButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSlotButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout schedulePanelLayout = new javax.swing.GroupLayout(schedulePanel);
        schedulePanel.setLayout(schedulePanelLayout);
        schedulePanelLayout.setHorizontalGroup(
            schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(schedulePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scheduleScrollPane)
                    .addGroup(schedulePanelLayout.createSequentialGroup()
                        .addComponent(addSlotButton)
                        .addGap(18, 18, 18)
                        .addComponent(removeSlotButton)))
                .addContainerGap())
        );
        schedulePanelLayout.setVerticalGroup(
            schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(schedulePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scheduleScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addSlotButton)
                    .addComponent(removeSlotButton))
                .addContainerGap())
        );

        tabs.addTab("Availability Schedule", schedulePanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void approveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_approveButtonActionPerformed
        Appointment selected = selectedAppointment();
        if (selected == null) return;
        doctorController.approve(selected.getId());
        refreshAppointments();
        UITheme.showMessageDialog(this, "Approved. Reception can now print the token/bill.",
                "Appointment Approved", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_approveButtonActionPerformed

    private void rejectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rejectButtonActionPerformed
        Appointment selected = selectedAppointment();
        if (selected == null) return;
        if (UITheme.showConfirmDialog(this, "Reject appointment " + selected.getAppointmentNumber() + "?",
                "Confirm Rejection") != JOptionPane.YES_OPTION) return;
        doctorController.reject(selected.getId());
        refreshAppointments();
    }//GEN-LAST:event_rejectButtonActionPerformed

    private void refreshAppointmentsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshAppointmentsButtonActionPerformed
        refreshAppointments();
    }//GEN-LAST:event_refreshAppointmentsButtonActionPerformed

    private void addSlotButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSlotButtonActionPerformed
        if (myProfile == null) return;
        ScheduleDialog dialog = new ScheduleDialog(this, doctorController, myProfile.getId());
        dialog.setVisible(true);
        if (dialog.wasAdded()) refreshSchedule();
    }//GEN-LAST:event_addSlotButtonActionPerformed

    private void removeSlotButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSlotButtonActionPerformed
        int row = scheduleTable.getSelectedRow();
        if (row == -1) return;
        if (UITheme.showConfirmDialog(this, "Remove the selected availability slot?",
                "Confirm Removal") != JOptionPane.YES_OPTION) return;
        int id = (int) scheduleModel.getValueAt(row, 0);
        doctorController.removeSlot(id);
        refreshSchedule();
    }//GEN-LAST:event_removeSlotButtonActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
         int choice = UITheme.showConfirmDialog(this,
            "Are you sure you want to log out?", "Confirm Logout");

         if (choice == JOptionPane.YES_OPTION) {
        new LoginView().setVisible(true);
        dispose();
        }
    }//GEN-LAST:event_logoutButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addSlotButton;
    private javax.swing.JPanel appointmentsPanel;
    private javax.swing.JButton approveButton;
    private javax.swing.JScrollPane apptScrollPane;
    private javax.swing.JTable apptTable;
    private javax.swing.JButton logoutButton;
    private javax.swing.JButton refreshAppointmentsButton;
    private javax.swing.JButton rejectButton;
    private javax.swing.JButton removeSlotButton;
    private javax.swing.JPanel schedulePanel;
    private javax.swing.JScrollPane scheduleScrollPane;
    private javax.swing.JTable scheduleTable;
    private javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables

    private Appointment selectedAppointment() {
        int row = apptTable.getSelectedRow();
        if (row == -1) {
            UITheme.showMessageDialog(this, "Select an appointment first", "No Selection", JOptionPane.WARNING_MESSAGE);
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
        java.time.LocalDate today = java.time.LocalDate.now();
        todayLabel.setText("Today: " + currentAppointments.stream()
                .filter(a -> a.getAppointmentDate().toLocalDate().equals(today)).count());
        pendingLabel.setText("Pending: " + currentAppointments.stream()
                .filter(a -> "PENDING".equals(a.getStatus())).count());
        acceptedLabel.setText("Accepted: " + currentAppointments.stream()
                .filter(a -> "ACCEPTED".equals(a.getStatus())).count());
    }

    private void refreshSchedule() {
        if (myProfile == null) return;
        currentSlots = doctorController.mySchedule(myProfile.getId());
        scheduleModel.setRowCount(0);
        for (Schedule s : currentSlots) {
            scheduleModel.addRow(new Object[]{
                    s.getId(), s.getDate(), s.getStartTime(), s.getEndTime(), s.isAvailable() ? "Yes" : "No"
            });
        }
        slotsLabel.setText("Available slots: " + currentSlots.stream().filter(Schedule::isAvailable).count());
    }
}
