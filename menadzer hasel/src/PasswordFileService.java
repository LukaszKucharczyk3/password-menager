import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class PasswordFileService {

    public void saveUserData(UserManager userManager) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            oos.writeObject(userManager.getUsers());
        }
    }

    @SuppressWarnings("unchecked")
    public List<User> loadUserData() throws IOException, ClassNotFoundException {
        File file = new File("users.dat");
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<User>) ois.readObject();
        }
    }


    public void savePasswords(List<IPasswordEntry> entries, String username) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(username + "_passwords.dat"))) {
            oos.writeObject(entries);
        }
    }

    @SuppressWarnings("unchecked")
    public List<IPasswordEntry> loadPasswords(String username) throws IOException, ClassNotFoundException {
        File file = new File(username + "_passwords.dat");
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<IPasswordEntry>) ois.readObject();
        }
    }
}
