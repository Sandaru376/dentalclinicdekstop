package view;

import controller.AdminController;
import model.DoctorProfile;
import model.TreatmentType;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboardView extends JFrame {

    private final AdminController adminController = new AdminController();
    private final User currentAdmin;

    private final DefaultTableModel doctorTableModel =
            new DefaultTableModel(new Object[]{"ID", "Name", "Specialization", "Contact", "Fee", "Available"}, 0) {
                public boolean isCellEditable(int r, int c) { return false; }
            };
    private final DefaultTableModel receptionTableModel =
            new DefaultTableModel(new Object[]{"ID", "Username", "Name", "Contact"}, 0) {
                public boolean isCellEditable(int r, int c) { return false; }
            };
    private final DefaultTableModel treatmentTableModel =
            new DefaultTableModel(new Object[]{"ID", "Name", "Cost"}, 0) {
                public boolean isCellEditable(int r, int c) { return false; }
            };

    private final JTable doctorTable = new JTable(doctorTableModel);
    private final JTable receptionTable = new JTable(receptionTableModel);
    private final JTable treatmentTable = new JTable(treatmentTableModel);

    public AdminDashboardView(User currentAdmin) {
        this.currentAdmin = currentAdmin;
        setTitle("Admin Dashboard - " + currentAdmin.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 520);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Doctors", buildDoctorsTab());
        tabs.addTab("Reception Staff", buildReceptionTab());
        tabs.addTab("Treatment Types & Prices", buildTreatmentTab());
        add(tabs);

        refreshAll();
    }

    // ---------- Doctors tab ----------
    private JPanel buildDoctorsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(doctorTable), BorderLayout.CENTER);

        JButton addButton = new JButton("Add Doctor");
        addButton.addActionListener(e -> {
            AddDoctorDialog dialog = new AddDoctorDialog(this, adminController);
            dialog.setVisible(true);
            if (dialog.wasCreated()) refreshDoctors();
        });

        JButton toggleButton = new JButton("Toggle Availability");
        toggleButton.addActionListener(e -> {
            int row = doctorTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a doctor first");
                return;
            }
            int id = (int) doctorTableModel.getValueAt(row, 0);
            boolean currentlyAvailable = "Yes".equals(doctorTableModel.getValueAt(row, 5));
            adminController.setDoctorAvailability(id, !currentlyAvailable);
            refreshDoctors();
        });

        JPanel buttons = new JPanel();
        buttons.add(addButton);
        buttons.add(toggleButton);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // ---------- Reception tab ----------
    private JPanel buildReceptionTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(receptionTable), BorderLayout.CENTER);

        JButton addButton = new JButton("Add Reception Staff");
        addButton.addActionListener(e -> {
            AddReceptionDialog dialog = new AddReceptionDialog(this, adminController);
            dialog.setVisible(true);
            if (dialog.wasCreated()) refreshReception();
        });

        JPanel buttons = new JPanel();
        buttons.add(addButton);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // ---------- Treatment types tab ----------
    private JPanel buildTreatmentTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(treatmentTable), BorderLayout.CENTER);

        JTextField nameField = new JTextField(12);
        JTextField costField = new JTextField(6);
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            try {
                adminController.addTreatmentType(nameField.getText().trim(), Double.parseDouble(costField.getText().trim()));
                nameField.setText("");
                costField.setText("");
                refreshTreatmentTypes();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Cost must be a number");
            }
        });

        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> {
            int row = treatmentTable.getSelectedRow();
            if (row == -1) return;
            int id = (int) treatmentTableModel.getValueAt(row, 0);
            adminController.deleteTreatmentType(id);
            refreshTreatmentTypes();
        });

        JPanel form = new JPanel();
        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(new JLabel("Cost (Rs.):")); form.add(costField);
        form.add(addButton);
        form.add(deleteButton);
        panel.add(form, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshAll() {
        refreshDoctors();
        refreshReception();
        refreshTreatmentTypes();
    }

    private void refreshDoctors() {
        doctorTableModel.setRowCount(0);
        List<DoctorProfile> doctors = adminController.listDoctors();
        for (DoctorProfile d : doctors) {
            doctorTableModel.addRow(new Object[]{
                    d.getId(), d.getFullName(), d.getSpecialization(), d.getContactNumber(),
                    d.getConsultationFee(), d.isAvailable() ? "Yes" : "No"
            });
        }
    }

    private void refreshReception() {
        receptionTableModel.setRowCount(0);
        List<User> receptionStaff = adminController.listReception();
        for (User u : receptionStaff) {
            receptionTableModel.addRow(new Object[]{u.getId(), u.getUsername(), u.getFullName(), u.getContactNumber()});
        }
    }

    private void refreshTreatmentTypes() {
        treatmentTableModel.setRowCount(0);
        List<TreatmentType> types = adminController.listTreatmentTypes();
        for (TreatmentType t : types) {
            treatmentTableModel.addRow(new Object[]{t.getId(), t.getName(), t.getCost()});
        }
    }
}
