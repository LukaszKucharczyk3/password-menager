public class StandardPasswordEntry extends PasswordEntryBase {
    public StandardPasswordEntry(String site, String username, String password) {
        super(site, username, password);
    }

    @Override
    public String toString() {
        return "Strona: " + site + ", Użytkownik: " + username + ", Hasło: " + password;
    }
}
