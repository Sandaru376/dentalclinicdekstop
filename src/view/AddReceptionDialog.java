package view;

import controller.AdminController;

import javax.swing.*;
import java.awt.*;

public class AddReceptionDialog extends JDialog {

    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JTextField fullNameField = new JTextField(15);
    private final JTextField contactField = new JTextField(15);
    private final AdminController adminController;
    private boolean created = false;

    public AddReceptionDialog(JFrame parent, AdminController adminController) {
        super(parent, "Add Reception Staff", true);
        this.adminController = adminController;
        setSize(340, 260);
        setLocationRelativeTo(parent);

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        form.add(new JLabel("Login username:")); form.add(usernameField);
        form.add(new JLabel("Temporary password:")); form.add(passwordField);
        form.add(new JLabel("Full name:")); form.add(fullNameField);
        form.add(new JLabel("Contact number:")); form.add(contactField);

        JButton saveButton = new JButton("Create Reception Account");
        saveButton.addActionListener(e -> onSave());

        JPanel bottom = new JPanel();
        bottom.add(saveButton);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void onSave() {
        try {
            adminController.createReception(
                    usernameField.getText().trim(),
                    new String(passwordField.getPassword()),
                    fullNameField.getText().trim(),
                    contactField.getText().trim()
            );
            created = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean wasCreated() { return created; }
}
