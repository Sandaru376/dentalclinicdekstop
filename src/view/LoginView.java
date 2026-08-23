package view;

import controller.LoginController;
import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * NOTE ON NETBEANS DRAG-AND-DROP:
 * This class builds its UI in code (not a paired .form file) so it is guaranteed
 * to compile and run correctly. To get a fully drag-and-drop editable version:
 *   1. In NetBeans: File -> New File -> Swing GUI Forms -> JFrame Form -> name it LoginView
 *   2. Drag on the same components (2 labels, 2 text fields, 1 button) from the Palette
 *   3. Double-click the Login button and paste the body of loginButtonActionPerformed() below
 * Everything else (controller/dao/model) stays exactly the same either way.
 */
public class LoginView extends JFrame {

    private final JTextField usernameField = new JTextField(18);
    private final JPasswordField passwordField = new JPasswordField(18);
    private final JLabel statusLabel = new JLabel(" ");
    private final LoginController loginController = new LoginController();

    public LoginView() {
        setTitle("Dental Clinic - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(380, 260);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Dental Clinic Management System", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> loginButtonActionPerformed());
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        statusLabel.setForeground(Color.RED);
        gbc.gridy = 4;
        panel.add(statusLabel, gbc);

        // Enter key in the password field triggers login too
        passwordField.addActionListener(e -> loginButtonActionPerformed());

        add(panel);
    }

    private void loginButtonActionPerformed() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter username and password");
            return;
        }

        User user = loginController.login(username, password);
        if (user == null) {
            statusLabel.setText("Invalid username or password");
            return;
        }

        JFrame nextScreen;
        switch (user.getRole()) {
            case "ADMIN":
                nextScreen = new AdminDashboardView(user);
                break;
            case "RECEPTION":
                nextScreen = new ReceptionDashboardView(user);
                break;
            case "DOCTOR":
                nextScreen = new DoctorDashboardView(user);
                break;
            default:
                statusLabel.setText("Unknown role: " + user.getRole());
                return;
        }
        nextScreen.setVisible(true);
        dispose();
    }
}
