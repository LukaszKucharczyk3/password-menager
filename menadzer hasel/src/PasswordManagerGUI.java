import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PasswordManagerGUI extends JFrame {
    private final String currentUser;
    private final PasswordManagerService service = new PasswordManagerService();
    private final DefaultListModel<IPasswordEntry> listModel = new DefaultListModel<>();
    private final JList<IPasswordEntry> entryList = new JList<>(listModel);

    private final JTextField siteField = new JTextField();
    private final JTextField userField = new JTextField();
    private final JPasswordField passField = new JPasswordField();
    private final JPasswordField confirmPassField = new JPasswordField();
    private final JTextField noteField = new JTextField();
    private final JComboBox<String> typeBox = new JComboBox<>(new String[]{"Standardowy", "Z notatką"});
    private final JComboBox<String> generatorTypeBox = new JComboBox<>(new String[]{"Silny", "Prosty", "Numeryczny"});
    private final JComboBox<Integer> passwordLengthBox = new JComboBox<>();
    private boolean editMode = false;
    private int editingIndex = -1;

    public PasswordManagerGUI(String username) {
        this.currentUser = username;
        setTitle("Menadżer Haseł - " + username);
        setSize(750, 500);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel addPanel = new JPanel(new GridLayout(9, 2));
        addPanel.add(new JLabel("Typ wpisu:"));
        addPanel.add(typeBox);
        addPanel.add(new JLabel("Strona:"));
        addPanel.add(siteField);
        addPanel.add(new JLabel("Użytkownik:"));
        addPanel.add(userField);
        addPanel.add(new JLabel("Hasło:"));
        addPanel.add(passField);
        addPanel.add(new JLabel("Potwierdź hasło:"));
        addPanel.add(confirmPassField);
        addPanel.add(new JLabel("Notatka (opcjonalnie):"));
        addPanel.add(noteField);
        addPanel.add(new JLabel("Typ generatora:"));
        addPanel.add(generatorTypeBox);
        addPanel.add(new JLabel("Długość hasła:"));
        for (int i = 6; i <= 32; i++) passwordLengthBox.addItem(i);
        passwordLengthBox.setSelectedItem(14);
        addPanel.add(passwordLengthBox);

        JButton generateButton = new JButton("Generuj hasło");
        addPanel.add(new JLabel());
        addPanel.add(generateButton);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj wpis");
        JButton editButton = new JButton("Edytuj");
        JButton removeButton = new JButton("Usuń");
        JButton saveButton = new JButton("Zapisz");
        JButton loadButton = new JButton("Wczytaj");
        JButton logoutButton = new JButton("Wyloguj");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(logoutButton);

        setLayout(new BorderLayout());
        add(addPanel, BorderLayout.NORTH);
        add(new JScrollPane(entryList), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);


        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (service.isModified()) {
                    int result = JOptionPane.showConfirmDialog(
                            PasswordManagerGUI.this,
                            "Masz niezapisane zmiany. Czy chcesz je zapisać przed zamknięciem?",
                            "Niezapisane zmiany",
                            JOptionPane.YES_NO_CANCEL_OPTION
                    );
                    if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                        return;
                    }
                    if (result == JOptionPane.YES_OPTION) {
                        try {
                            service.savePasswords(currentUser);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(PasswordManagerGUI.this, "Błąd zapisu: " + ex.getMessage());
                            return;
                        }
                    }
                }
                dispose();
                System.exit(0);
            }
        });


        try {
            service.loadPasswords(currentUser);
            listModel.clear();
            for (IPasswordEntry entry : service.getEntries()) listModel.addElement(entry);
        } catch (Exception e) {}


        entryList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                IPasswordEntry entry = (IPasswordEntry) value;
                String display = "Strona: " + entry.getSite() + ", Użytkownik: " + entry.getUsername() + ", Hasło: ********";
                if (entry instanceof SecurePasswordEntry) {
                    display += ", Notatka: " + ((SecurePasswordEntry) entry).getNote();
                }
                return super.getListCellRendererComponent(list, display, index, isSelected, cellHasFocus);
            }
        });


        entryList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int idx = entryList.locationToIndex(evt.getPoint());
                    if (idx >= 0) {
                        JPasswordField masterKeyField = new JPasswordField();
                        int action = JOptionPane.showConfirmDialog(
                                PasswordManagerGUI.this,
                                masterKeyField,
                                "Podaj klucz główny, aby zobaczyć hasło:",
                                JOptionPane.OK_CANCEL_OPTION
                        );
                        if (action == JOptionPane.OK_OPTION) {
                            String masterKey = new String(masterKeyField.getPassword());
                            UserManager userManager = new UserManager();
                            try {
                                userManager.setUsers(new PasswordFileService().loadUserData());
                            } catch (Exception e) {}
                            if (userManager.validateMasterKey(currentUser, masterKey)) {
                                IPasswordEntry entry = listModel.get(idx);
                                JOptionPane.showMessageDialog(PasswordManagerGUI.this,
                                        "Hasło do " + entry.getSite() + " to: " + entry.getPassword());
                            } else {
                                JOptionPane.showMessageDialog(PasswordManagerGUI.this, "Nieprawidłowy klucz główny!");
                            }
                        }
                    }
                }
            }
        });


        removeButton.addActionListener(e -> {
            int idx = entryList.getSelectedIndex();
            if (idx >= 0) {
                JPasswordField masterKeyField = new JPasswordField();
                int action = JOptionPane.showConfirmDialog(
                        this, masterKeyField, "Podaj klucz główny", JOptionPane.OK_CANCEL_OPTION);
                if (action == JOptionPane.OK_OPTION) {
                    String masterKey = new String(masterKeyField.getPassword());
                    UserManager userManager = new UserManager();
                    try {
                        userManager.setUsers(new PasswordFileService().loadUserData());
                    } catch (Exception ex) {}
                    if (userManager.validateMasterKey(currentUser, masterKey)) {
                        try {
                            service.removeEntry(idx);
                            listModel.remove(idx);
                        } catch (PasswordManagerException ex) {
                            JOptionPane.showMessageDialog(this, ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Nieprawidłowy klucz główny!");
                    }
                }
            }
        });


        editButton.addActionListener(e -> {
            int idx = entryList.getSelectedIndex();
            if (idx >= 0) {
                JPasswordField masterKeyField = new JPasswordField();
                int action = JOptionPane.showConfirmDialog(
                        this, masterKeyField, "Podaj klucz główny", JOptionPane.OK_CANCEL_OPTION);
                if (action == JOptionPane.OK_OPTION) {
                    String masterKey = new String(masterKeyField.getPassword());
                    UserManager userManager = new UserManager();
                    try {
                        userManager.setUsers(new PasswordFileService().loadUserData());
                    } catch (Exception ex) {}
                    if (userManager.validateMasterKey(currentUser, masterKey)) {
                        IPasswordEntry selected = listModel.get(idx);
                        siteField.setText(selected.getSite());
                        userField.setText(selected.getUsername());
                        passField.setText(selected.getPassword());
                        confirmPassField.setText(selected.getPassword());
                        if (selected instanceof SecurePasswordEntry) {
                            noteField.setText(((SecurePasswordEntry) selected).getNote());
                            typeBox.setSelectedIndex(1);
                        } else {
                            noteField.setText("");
                            typeBox.setSelectedIndex(0);
                        }
                        addButton.setText("Zapisz zmiany");
                        editMode = true;
                        editingIndex = idx;
                    } else {
                        JOptionPane.showMessageDialog(this, "Nieprawidłowy klucz główny!");
                    }
                }
            }
        });

        addButton.addActionListener(e -> handleAddEdit(addButton));
        saveButton.addActionListener(e -> handleSave());
        loadButton.addActionListener(e -> handleLoad());
        logoutButton.addActionListener(e -> handleLogout());
        generateButton.addActionListener(e -> handleGenerate());
    }

    private void handleAddEdit(JButton addButton) {
        try {
            String site = siteField.getText();
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());
            String note = noteField.getText();

            if (pass.trim().isEmpty() || !pass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "Hasła nie są takie same lub są puste!");
                return;
            }

            IPasswordEntry entry;
            if (typeBox.getSelectedIndex() == 0) {
                entry = new StandardPasswordEntry(site, user, pass);
            } else {
                entry = new SecurePasswordEntry(site, user, pass, note);
            }

            if (!editMode) {
                service.addEntry(entry);
                listModel.addElement(entry);
            } else {
                service.updateEntry(editingIndex, entry);
                listModel.set(editingIndex, entry);
                addButton.setText("Dodaj wpis");
                editMode = false;
                editingIndex = -1;
                entryList.clearSelection();
            }
            clearFields();
        } catch (PasswordManagerException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void handleSave() {
        try {
            service.savePasswords(currentUser);
            JOptionPane.showMessageDialog(this, "Zapisano dane!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Błąd zapisu: " + ex.getMessage());
        }
    }

    private void handleLoad() {
        try {
            service.loadPasswords(currentUser);
            listModel.clear();
            for (IPasswordEntry entry : service.getEntries()) listModel.addElement(entry);
            JOptionPane.showMessageDialog(this, "Wczytano dane!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Błąd wczytywania: " + ex.getMessage());
        }
    }

    private void handleLogout() {
        if (service.isModified()) {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Masz niezapisane zmiany. Czy chcesz je zapisać przed wylogowaniem?",
                    "Niezapisane zmiany",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );
            if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                return;
            }
            if (result == JOptionPane.YES_OPTION) {
                try {
                    service.savePasswords(currentUser);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Błąd zapisu: " + ex.getMessage());
                    return;
                }
            }
        }
        new LoginGUI().setVisible(true);
        dispose();
    }

    private void handleGenerate() {
        int length = (Integer) passwordLengthBox.getSelectedItem();
        PasswordGenerator generator;
        String type = (String) generatorTypeBox.getSelectedItem();
        if ("Prosty".equals(type)) {
            generator = new SimplePasswordGenerator();
        } else if ("Numeryczny".equals(type)) {
            generator = new NumericPasswordGenerator();
        } else {
            generator = new StrongPasswordGenerator();
        }
        String generated = generator.generate(length);
        passField.setText(generated);
        confirmPassField.setText(generated);
    }

    private void clearFields() {
        siteField.setText("");
        userField.setText("");
        passField.setText("");
        confirmPassField.setText("");
        noteField.setText("");
    }
}
