package view;

import controller.ReceptionController;
import model.Appointment;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReceptionDashboardView extends JFrame {

    private final ReceptionController receptionController = new ReceptionController();
    private final User currentUser;

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Appt #", "Patient", "Doctor", "Treatment", "Date", "Time", "Status", "Token", "Total"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private List<Appointment> currentList;

    public ReceptionDashboardView(User currentUser) {
        this.currentUser = currentUser;
        setTitle("Reception Dashboard - " + currentUser.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 520);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton bookButton = new JButton("Book Appointment");
        bookButton.addActionListener(e -> {
            BookAppointmentDialog dialog = new BookAppointmentDialog(this, receptionController, currentUser);
            dialog.setVisible(true);
            if (dialog.wasBooked()) refresh();
        });

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refresh());

        JButton billButton = new JButton("Print Bill / Token");
        billButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an appointment first");
                return;
            }
            Appointment appt = currentList.get(row);
            if (!"ACCEPTED".equals(appt.getStatus())) {
                JOptionPane.showMessageDialog(this, "The doctor hasn't approved this appointment yet.");
                return;
            }
            new BillView(this, appt).setVisible(true);
        });

        JPanel buttons = new JPanel();
        buttons.add(bookButton);
        buttons.add(refreshButton);
        buttons.add(billButton);
        panel.add(buttons, BorderLayout.SOUTH);

        add(panel);
        refresh();
    }

    private void refresh() {
        currentList = receptionController.allAppointments();
        model.setRowCount(0);
        for (Appointment a : currentList) {
            model.addRow(new Object[]{
                    a.getAppointmentNumber(), a.getPatientName(), a.getDoctorName(), a.getTreatmentTypeName(),
                    a.getAppointmentDate(), a.getAppointmentTime(), a.getStatus(),
                    a.getTokenNumber() == null ? "" : a.getTokenNumber(),
                    a.getTotalCost()
            });
        }
    }
}
