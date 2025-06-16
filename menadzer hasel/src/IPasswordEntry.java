import java.io.Serializable;

public interface IPasswordEntry extends Serializable {
    String getSite();
    String getUsername();
    String getPassword();
}
