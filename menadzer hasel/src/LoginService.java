import java.util.List;

public class LoginService {
    private final UserManager userManager = new UserManager();
    private final PasswordFileService fileService = new PasswordFileService();

    public LoginService() {
        try {
            List<User> loadedUsers = fileService.loadUserData();
            userManager.setUsers(loadedUsers);
        } catch (Exception e) {}
    }

    public boolean validateUser(String username, String password) {
        return userManager.validateUser(username, password);
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public PasswordFileService getFileService() {
        return fileService;
    }
}
