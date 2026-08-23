package view;

import controller.AdminController;

import javax.swing.*;
import java.awt.*;

public class AddDoctorDialog extends JDialog {

    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JTextField fullNameField = new JTextField(15);
    private final JTextField specializationField = new JTextField(15);
    private final JTextField contactField = new JTextField(15);
    private final JTextField feeField = new JTextField(15);
    private final AdminController adminController;
    private boolean created = false;

    public AddDoctorDialog(JFrame parent, AdminController adminController) {
        super(parent, "Add Doctor", true);
        this.adminController = adminController;
        setSize(360, 340);
        setLocationRelativeTo(parent);

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        form.add(new JLabel("Login username:")); form.add(usernameField);
        form.add(new JLabel("Temporary password:")); form.add(passwordField);
        form.add(new JLabel("Full name:")); form.add(fullNameField);
        form.add(new JLabel("Specialization:")); form.add(specializationField);
        form.add(new JLabel("Contact number:")); form.add(contactField);
        form.add(new JLabel("Consultation fee (Rs.):")); form.add(feeField);

        JButton saveButton = new JButton("Create Doctor Account");
        saveButton.addActionListener(e -> onSave());

        JPanel bottom = new JPanel();
        bottom.add(saveButton);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void onSave() {
        try {
            String fee = feeField.getText().trim().isEmpty() ? "0" : feeField.getText().trim();
            adminController.createDoctor(
                    usernameField.getText().trim(),
                    new String(passwordField.getPassword()),
                    fullNameField.getText().trim(),
                    specializationField.getText().trim(),
                    contactField.getText().trim(),
                    Double.parseDouble(fee)
            );
            created = true;
            dispose();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Consultation fee must be a number", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean wasCreated() { return created; }
}
