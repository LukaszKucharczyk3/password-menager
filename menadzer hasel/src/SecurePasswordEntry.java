public class SecurePasswordEntry extends PasswordEntryBase {
    private String note;

    public SecurePasswordEntry(String site, String username, String password, String note) {
        super(site, username, password);
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "Strona: " + site + ", Użytkownik: " + username + ", Hasło: " + password + ", Notatka: " + note;
    }
}
