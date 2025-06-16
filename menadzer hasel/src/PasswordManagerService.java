import java.util.List;

public class PasswordManagerService {
    private final PasswordManager manager = new PasswordManager();
    private final PasswordFileService fileService = new PasswordFileService();
    private boolean isModified = false;

    public List<IPasswordEntry> getEntries() {
        return manager.getEntries();
    }

    public void loadPasswords(String username) throws Exception {
        List<IPasswordEntry> loaded = fileService.loadPasswords(username);
        manager.setEntries(loaded);
        isModified = false;
    }

    public void savePasswords(String username) throws Exception {
        fileService.savePasswords(manager.getEntries(), username);
        isModified = false;
    }

    public void addEntry(IPasswordEntry entry) throws PasswordManagerException {
        manager.addEntry(entry);
        isModified = true;
    }

    public void updateEntry(int idx, IPasswordEntry entry) throws PasswordManagerException {
        manager.getEntries().set(idx, entry);
        isModified = true;
    }

    public void removeEntry(int idx) throws PasswordManagerException {
        manager.removeEntry(idx);
        isModified = true;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }
}
