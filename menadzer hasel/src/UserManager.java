import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UserManager implements Serializable {
    private List<User> users = new ArrayList<>();

    public void registerUser(String username, String password, String masterKey) throws PasswordManagerException {
        if (username == null || username.trim().isEmpty())
            throw new PasswordManagerException("Nazwa użytkownika nie może być pusta!");
        if (password == null || password.trim().isEmpty())
            throw new PasswordManagerException("Hasło nie może być puste!");
        if (masterKey == null || masterKey.trim().isEmpty())
            throw new PasswordManagerException("Klucz główny nie może być pusty!");
        if (userExists(username))
            throw new PasswordManagerException("Użytkownik już istnieje!");
        users.add(new User(username, hashPassword(password), hashPassword(masterKey)));
    }

    public boolean userExists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public boolean validateUser(String username, String password) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username) &&
                u.getHashedPassword().equals(hashPassword(password)));
    }

    public boolean validateMasterKey(String username, String masterKey) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username) &&
                u.getHashedMasterKey().equals(hashPassword(masterKey)));
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Brak algorytmu SHA-256");
        }
    }
}
