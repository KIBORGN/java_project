package Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import Client.Client;

public class ClientRepository {
    private File file;

    public ClientRepository(String path) {
        this.file = new File(path);
    }

    @SuppressWarnings("unchecked")
    public List<Client> loadClients() {
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Client>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public void saveClients(List<Client> clients) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(clients);
        } catch (IOException ignored) {
        }
    }
}