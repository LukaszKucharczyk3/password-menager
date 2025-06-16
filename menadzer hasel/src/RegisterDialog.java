import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class RegisterDialog extends JDialog {
    private JTextField usernameField = new JTextField(15);
    private JPasswordField passwordField = new JPasswordField(15);
    private JPasswordField confirmPasswordField = new JPasswordField(15);
    private JPasswordField masterKeyField = new JPasswordField(15);

    public RegisterDialog(JFrame parent, UserManager userManager, PasswordFileService fileService) {
        super(parent, "Rejestracja", true);
        setSize(350, 280);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Nazwa użytkownika:"));
        panel.add(usernameField);
        panel.add(new JLabel("Hasło:"));
        panel.add(passwordField);
        panel.add(new JLabel("Potwierdź hasło:"));
        panel.add(confirmPasswordField);
        panel.add(new JLabel("Klucz główny:"));
        panel.add(masterKeyField);

        JButton registerButton = new JButton("Zarejestruj");
        panel.add(new JLabel());
        panel.add(registerButton);

        add(panel);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String masterKey = new String(masterKeyField.getPassword());

            if (username.trim().isEmpty() || password.trim().isEmpty() || masterKey.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wszystkie pola muszą być wypełnione!");
                return;
            }
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Hasła nie są takie same!");
                return;
            }
            try {
                userManager.registerUser(username, password, masterKey);
                fileService.saveUserData(userManager);
                JOptionPane.showMessageDialog(this, "Rejestracja udana!");
                dispose();
            } catch (PasswordManagerException | IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
    }
}
