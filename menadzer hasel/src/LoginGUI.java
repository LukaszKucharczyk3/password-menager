import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JFrame {
    private JTextField usernameField = new JTextField(15);
    private JPasswordField passwordField = new JPasswordField(15);
    private final LoginService loginService = new LoginService();

    public LoginGUI() {
        setTitle("Logowanie");
        setSize(350, 170);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Nazwa użytkownika:"));
        panel.add(usernameField);
        panel.add(new JLabel("Hasło:"));
        panel.add(passwordField);

        JButton loginButton = new JButton("Zaloguj");
        JButton registerButton = new JButton("Zarejestruj");

        panel.add(loginButton);
        panel.add(registerButton);
        add(panel);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (loginService.validateUser(username, password)) {
                new PasswordManagerGUI(username).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Nieprawidłowy login lub hasło!");
            }
        });

        registerButton.addActionListener(e -> {
            new RegisterDialog(this, loginService.getUserManager(), loginService.getFileService()).setVisible(true);
        });
    }
}
