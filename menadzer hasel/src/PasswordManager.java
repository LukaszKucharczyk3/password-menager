import java.util.ArrayList;
import java.util.List;

public class PasswordManager {
    private List<IPasswordEntry> entries;

    public PasswordManager() {
        entries = new ArrayList<>();
    }

    public void addEntry(IPasswordEntry entry) throws PasswordManagerException {
        if (entry == null) throw new PasswordManagerException("Wpis nie może być pusty!");
        entries.add(entry);
    }

    public void removeEntry(int index) throws PasswordManagerException {
        if (index < 0 || index >= entries.size())
            throw new PasswordManagerException("Nieprawidłowy indeks!");
        entries.remove(index);
    }

    public List<IPasswordEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<IPasswordEntry> entries) {
        this.entries = entries;
    }
}
