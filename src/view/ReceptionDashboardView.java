package view;

import controller.ReceptionController;
import model.Appointment;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;

/**
 * NetBeans GUI Builder form (paired ReceptionDashboardView.form) - edit via Design tab.
 */
public class ReceptionDashboardView extends JFrame {

    private final ReceptionController receptionController = new ReceptionController();
    private final User currentUser;
    private List<Appointment> currentList;
    private final JLabel todayCountLabel = new JLabel("Today: 0", SwingConstants.CENTER);
    private final JLabel pendingCountLabel = new JLabel("Pending: 0", SwingConstants.CENTER);
    private final JLabel acceptedCountLabel = new JLabel("Accepted: 0", SwingConstants.CENTER);
    private final JLabel rejectedCountLabel = new JLabel("Rejected: 0", SwingConstants.CENTER);
    private final JTextField appointmentSearchField = new JTextField(22);
    private final JComboBox<String> statusFilter = new JComboBox<>(new String[]{"All statuses", "PENDING", "ACCEPTED", "REJECTED"});
    private TableRowSorter<DefaultTableModel> appointmentSorter;

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Appt #", "Patient", "Doctor", "Treatment", "Date", "Time", "Status", "Token", "Total"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };

    public ReceptionDashboardView(User currentUser) {
        this.currentUser = currentUser;
        setTitle("Reception Dashboard - " + currentUser.getFullName());
        initComponents();
        appointmentTable.setModel(model);
        createDashboardTabs();
        UITheme.setWindowIcon(this, "/resources/stafficon.png");
        UITheme.style(this);
        UITheme.installEmptyState(appointmentScrollPane, appointmentTable,
                "No appointments match the selected filters.");
        setMinimumSize(new java.awt.Dimension(900, 540));
        setSize(1020, 600);
        setLocationRelativeTo(null);
        refresh();
    }

    private void createDashboardTabs() {
        java.awt.Container appointmentsPanel = getContentPane();
        JTabbedPane dashboardTabs = new JTabbedPane();
        dashboardTabs.setOpaque(true);
        dashboardTabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        JPanel appointmentsPage = new JPanel(new java.awt.BorderLayout(0, 10));
        appointmentsPage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel filters = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        filters.add(new JLabel("Search:"));
        appointmentSearchField.setToolTipText("Search by appointment, patient, doctor, or treatment");
        filters.add(appointmentSearchField);
        filters.add(new JLabel("Status:"));
        filters.add(statusFilter);
        appointmentsPage.add(filters, java.awt.BorderLayout.NORTH);
        appointmentsPage.add(appointmentsPanel, java.awt.BorderLayout.CENTER);
        dashboardTabs.addTab("Appointments",
                UITheme.resourceIcon("/resources/dentalicon.png", 20, 20), appointmentsPage);
        dashboardTabs.addTab("Appointment Search",
                UITheme.resourceIcon("/resources/dentalicon.png", 20, 20), new AppointmentSearchPanel());
        dashboardTabs.addTab("Help",
                UITheme.resourceIcon("/resources/stafficon.png", 20, 20), new HelpPanel());

        JPanel header = new JPanel(new java.awt.BorderLayout(12, 0));
        header.setBackground(UITheme.SURFACE_ALT);
        header.putClientProperty("ui.preserveBackground", Boolean.TRUE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        JLabel heading = new JLabel("Reception Dashboard");
        heading.setFont(heading.getFont().deriveFont(java.awt.Font.BOLD, 21f));
        heading.setForeground(UITheme.TEXT);
        heading.putClientProperty("ui.preserveForeground", Boolean.TRUE);
        ImageIcon staffIcon = UITheme.resourceIcon("/resources/stafficon.png", 34, 34);
        if (staffIcon != null) {
            heading.setIcon(staffIcon);
            heading.setIconTextGap(11);
        }
        JLabel signedIn = new JLabel("Signed in as  " + currentUser.getFullName());
        signedIn.setForeground(UITheme.MUTED);
        signedIn.putClientProperty("ui.preserveForeground", Boolean.TRUE);
        header.add(heading, java.awt.BorderLayout.WEST);
        header.add(signedIn, java.awt.BorderLayout.EAST);

        JPanel summaries = new JPanel(new java.awt.GridLayout(1, 4, 10, 0));
        summaries.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        summaries.add(summaryCard(todayCountLabel, UITheme.ACCENT));
        summaries.add(summaryCard(pendingCountLabel, new java.awt.Color(251, 191, 36)));
        summaries.add(summaryCard(acceptedCountLabel, new java.awt.Color(34, 197, 94)));
        summaries.add(summaryCard(rejectedCountLabel, UITheme.DANGER));
        JPanel top = new JPanel(new java.awt.BorderLayout());
        top.add(header, java.awt.BorderLayout.NORTH);
        top.add(summaries, java.awt.BorderLayout.CENTER);

        appointmentSorter = new TableRowSorter<>(model);
        appointmentTable.setRowSorter(appointmentSorter);
        appointmentSearchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent event) { applyAppointmentFilters(); }
            public void removeUpdate(javax.swing.event.DocumentEvent event) { applyAppointmentFilters(); }
            public void changedUpdate(javax.swing.event.DocumentEvent event) { applyAppointmentFilters(); }
        });
        statusFilter.addActionListener(event -> applyAppointmentFilters());

        JPanel shell = new JPanel(new java.awt.BorderLayout());
        shell.add(top, java.awt.BorderLayout.NORTH);
        shell.add(dashboardTabs, java.awt.BorderLayout.CENTER);
        setContentPane(shell);
    }

    private JPanel summaryCard(JLabel label, java.awt.Color accent) {
        label.setFont(label.getFont().deriveFont(java.awt.Font.BOLD, 16f));
        JPanel card = new JPanel(new java.awt.BorderLayout());
        card.setBackground(UITheme.SURFACE_ALT);
        card.putClientProperty("ui.preserveBackground", Boolean.TRUE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 1, 1, 1, accent),
                BorderFactory.createEmptyBorder(10, 8, 10, 8)));
        card.add(label);
        return card;
    }

    private void applyAppointmentFilters() {
        if (appointmentSorter == null) return;
        java.util.List<RowFilter<DefaultTableModel, Object>> filters = new java.util.ArrayList<>();
        String search = appointmentSearchField.getText().trim();
        if (!search.isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(search)));
        String status = (String) statusFilter.getSelectedItem();
        if (status != null && !status.startsWith("All")) filters.add(RowFilter.regexFilter("^" + status + "$", 6));
        appointmentSorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        appointmentScrollPane = new javax.swing.JScrollPane();
        appointmentTable = new javax.swing.JTable();
        bookAppointmentButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        printBillButton = new javax.swing.JButton();
        logoutButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        appointmentScrollPane.setViewportView(appointmentTable);

        bookAppointmentButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bookAppointmentButton.setText("Book Appointment");
        bookAppointmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookAppointmentButtonActionPerformed(evt);
            }
        });

        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        printBillButton.setText("Print Bill / Token");
        printBillButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printBillButtonActionPerformed(evt);
            }
        });

        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(appointmentScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bookAppointmentButton)
                        .addGap(18, 18, 18)
                        .addComponent(refreshButton)
                        .addGap(18, 18, 18)
                        .addComponent(printBillButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(logoutButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(appointmentScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bookAppointmentButton)
                    .addComponent(refreshButton)
                    .addComponent(printBillButton)
                    .addComponent(logoutButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bookAppointmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookAppointmentButtonActionPerformed
        BookAppointmentDialog dialog = new BookAppointmentDialog(this, receptionController, currentUser);
        dialog.setVisible(true);
        if (dialog.wasBooked()) refresh();
    }//GEN-LAST:event_bookAppointmentButtonActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        refresh();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void printBillButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printBillButtonActionPerformed
        int row = appointmentTable.getSelectedRow();
        if (row == -1) {
            UITheme.showMessageDialog(this, "Select an appointment first", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Appointment appt = currentList.get(appointmentTable.convertRowIndexToModel(row));
        if (!"ACCEPTED".equals(appt.getStatus())) {
            UITheme.showMessageDialog(this, "The doctor hasn't approved this appointment yet.",
                    "Bill Unavailable", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new BillView(this, appt).setVisible(true);
    }//GEN-LAST:event_printBillButtonActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        int choice = UITheme.showConfirmDialog(this,
            "Are you sure you want to log out?", "Confirm Logout");

    if (choice == JOptionPane.YES_OPTION) {
        new LoginView().setVisible(true);
        dispose();
    }
    }//GEN-LAST:event_logoutButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane appointmentScrollPane;
    private javax.swing.JTable appointmentTable;
    private javax.swing.JButton bookAppointmentButton;
    private javax.swing.JButton logoutButton;
    private javax.swing.JButton printBillButton;
    private javax.swing.JButton refreshButton;
    // End of variables declaration//GEN-END:variables

    private void refresh() {
        currentList = receptionController.allAppointments();
        model.setRowCount(0);
        for (Appointment a : currentList) {
            model.addRow(new Object[]{
                    a.getAppointmentNumber(), a.getPatientName(), a.getDoctorName(), a.getTreatmentTypeName(),
                    a.getAppointmentDate(), a.getAppointmentTime(), a.getStatus(),
                    a.getTokenNumber() == null ? "" : a.getTokenNumber(),
                    String.format("Rs. %,.2f", a.getTotalCost())
            });
        }
        java.time.LocalDate today = java.time.LocalDate.now();
        long todayCount = currentList.stream().filter(a -> a.getAppointmentDate().toLocalDate().equals(today)).count();
        long pending = currentList.stream().filter(a -> "PENDING".equals(a.getStatus())).count();
        long accepted = currentList.stream().filter(a -> "ACCEPTED".equals(a.getStatus())).count();
        long rejected = currentList.stream().filter(a -> "REJECTED".equals(a.getStatus())).count();
        todayCountLabel.setText("Today: " + todayCount);
        pendingCountLabel.setText("Pending: " + pending);
        acceptedCountLabel.setText("Accepted: " + accepted);
        rejectedCountLabel.setText("Rejected: " + rejected);
    }
}
