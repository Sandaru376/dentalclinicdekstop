package view;

import model.Appointment;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;

/** Shown once a doctor has ACCEPTED an appointment: the token to hand the patient + the bill. */
public class BillView extends JDialog {

    private final JTextArea billArea = new JTextArea();

    public BillView(JFrame parent, Appointment appt) {
        super(parent, "Bill / Token - " + appt.getAppointmentNumber(), true);
        setSize(420, 420);
        setLocationRelativeTo(parent);

        billArea.setEditable(false);
        billArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        billArea.setText(buildBillText(appt));

        JButton printButton = new JButton("Print");
        printButton.addActionListener(e -> {
            try {
                billArea.print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Could not print: " + ex.getMessage());
            }
        });
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JPanel bottom = new JPanel();
        bottom.add(printButton);
        bottom.add(closeButton);

        setLayout(new BorderLayout());
        add(new JScrollPane(billArea), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private String buildBillText(Appointment a) {
        StringBuilder sb = new StringBuilder();
        sb.append("        DENTAL CLINIC - RECEIPT\n");
        sb.append("========================================\n");
        sb.append("Appointment No : ").append(a.getAppointmentNumber()).append("\n");
        sb.append("Token No       : ").append(a.getTokenNumber() == null ? "(not yet issued)" : a.getTokenNumber()).append("\n");
        sb.append("Patient        : ").append(a.getPatientName()).append("\n");
        sb.append("Contact        : ").append(a.getPatientContact()).append("\n");
        sb.append("Doctor         : ").append(a.getDoctorName()).append("\n");
        sb.append("Treatment      : ").append(a.getTreatmentTypeName()).append("\n");
        sb.append("Date / Time    : ").append(a.getAppointmentDate()).append("  ").append(a.getAppointmentTime()).append("\n");
        sb.append("----------------------------------------\n");
        sb.append(String.format("Consultation Fee : Rs. %.2f%n", a.getConsultationFee()));
        sb.append(String.format("Treatment Cost   : Rs. %.2f%n", a.getTreatmentCost()));
        sb.append("----------------------------------------\n");
        sb.append(String.format("TOTAL            : Rs. %.2f%n", a.getTotalCost()));
        sb.append("========================================\n");
        sb.append("Status: ").append(a.getStatus()).append("\n");
        return sb.toString();
    }
}
