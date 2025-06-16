import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String hashedPassword;
    private String hashedMasterKey;

    public User(String username, String hashedPassword, String hashedMasterKey) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.hashedMasterKey = hashedMasterKey;
    }

    public String getUsername() { return username; }
    public String getHashedPassword() { return hashedPassword; }
    public String getHashedMasterKey() { return hashedMasterKey; }
}
