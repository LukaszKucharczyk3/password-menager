import com.formdev.flatlaf.FlatDarculaLaf;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        UIManager.put("Button.arc", 20);
        UIManager.put("Component.arc", 18);
        UIManager.put("TextComponent.arc", 15);
        UIManager.put("Button.font", new javax.swing.plaf.FontUIResource("Segoe UI", java.awt.Font.BOLD, 15));

        FlatDarculaLaf.setup();

        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }
}
