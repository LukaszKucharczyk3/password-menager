public abstract class PasswordEntryBase implements IPasswordEntry {
    protected String site;
    protected String username;
    protected String password;

    public PasswordEntryBase(String site, String username, String password) {
        this.site = site;
        this.username = username;
        this.password = password;
    }

    @Override
    public String getSite() { return site; }

    @Override
    public String getUsername() { return username; }

    @Override
    public String getPassword() { return password; }
}
