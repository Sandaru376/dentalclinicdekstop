/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import controller.AppointmentSearchController;
import model.Appointment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 *
 * @author Admin
 */
public class AppointmentSearchPanel extends javax.swing.JPanel {

    private final AppointmentSearchController controller =
            new AppointmentSearchController();

    private JTextField appointmentNumberField;
    private JButton searchButton;
    private JButton clearButton;
    private JTextArea detailsArea;

    /**
     * Creates new form AppointmentSearchPanel
     */
    public AppointmentSearchPanel() {
        initComponents();
        UITheme.stylePanel(this);
        buildSearchInterface();
    }

    private void buildSearchInterface() {
        removeAll();
        setLayout(new BorderLayout(12, 12));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(new Color(244, 247, 251));

        JLabel titleLabel = new JLabel("Search Appointment");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel numberLabel = new JLabel("Appointment Number:");
        appointmentNumberField = new JTextField("APT-", 18);
        appointmentNumberField.setToolTipText("Example: APT-000001");

        searchButton = new JButton("Search");
        clearButton = new JButton("Clear");

        searchButton.addActionListener(event -> searchAppointment());
        clearButton.addActionListener(event -> clearSearch());
        appointmentNumberField.addActionListener(event -> searchAppointment());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createTitledBorder("Find an appointment"));
        searchPanel.add(numberLabel);
        searchPanel.add(appointmentNumberField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setOpaque(false);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        detailsArea.setMargin(new Insets(15, 15, 15, 15));
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setText("Enter an appointment number and click Search.");

        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);
        detailsScrollPane.setBorder(
                BorderFactory.createTitledBorder("Appointment Details"));

        add(topPanel, BorderLayout.NORTH);
        add(detailsScrollPane, BorderLayout.CENTER);
    }

    private void searchAppointment() {
        String number = appointmentNumberField.getText().trim();

        if (number.isEmpty() || "APT-".equalsIgnoreCase(number)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a complete appointment number.",
                    "Appointment Number Required",
                    JOptionPane.WARNING_MESSAGE);
            appointmentNumberField.requestFocusInWindow();
            return;
        }

        try {
            Appointment appointment = controller.search(number);

            if (appointment == null) {
                detailsArea.setText("No appointment found.");
                JOptionPane.showMessageDialog(
                        this,
                        "No appointment found with number: " + number,
                        "Not Found",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            displayAppointment(appointment);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not search for the appointment.\n" + exception.getMessage(),
                    "Search Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayAppointment(Appointment appointment) {
        StringBuilder details = new StringBuilder();
        details.append("APPOINTMENT DETAILS\n");
        details.append("================================\n\n");
        addDetail(details, "Appointment Number", appointment.getAppointmentNumber());
        addDetail(details, "Patient Name", appointment.getPatientName());
        addDetail(details, "Patient Contact", appointment.getPatientContact());
        addDetail(details, "Doctor", appointment.getDoctorName());
        addDetail(details, "Treatment", appointment.getTreatmentTypeName());
        addDetail(details, "Date", appointment.getAppointmentDate());
        addDetail(details, "Time", appointment.getAppointmentTime());
        addDetail(details, "Status", appointment.getStatus());
        addDetail(details, "Token Number", appointment.getTokenNumber());
        addDetail(details, "Consultation Fee", formatMoney(appointment.getConsultationFee()));
        addDetail(details, "Treatment Cost", formatMoney(appointment.getTreatmentCost()));
        addDetail(details, "Total Cost", formatMoney(appointment.getTotalCost()));
        addDetail(details, "Booked By", appointment.getCreatedBy());
        addDetail(details, "Created At", appointment.getCreatedAt());
        addDetail(details, "Notes", appointment.getNotes());

        detailsArea.setText(details.toString());
        detailsArea.setCaretPosition(0);
    }

    private void addDetail(StringBuilder builder, String label, Object value) {
        builder.append(String.format("%-20s: %s%n", label, safeText(value)));
    }

    private String safeText(Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            return "-";
        }
        return value.toString();
    }

    private String formatMoney(double amount) {
        return String.format("Rs. %.2f", amount);
    }

    private void clearSearch() {
        appointmentNumberField.setText("APT-");
        detailsArea.setText("Enter an appointment number and click Search.");
        appointmentNumberField.requestFocusInWindow();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
